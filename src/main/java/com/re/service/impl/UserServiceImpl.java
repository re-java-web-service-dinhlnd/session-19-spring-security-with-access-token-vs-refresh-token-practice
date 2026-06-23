package com.re.service.impl;

import com.re.dto.UserDetailsDto;
import com.re.dto.UserProfileDto;
import com.re.dto.request.UserUpdateRequest;
import com.re.dto.response.UserUpdateResponse;
import com.re.entity.User;
import com.re.exception.BadRequestException;
import com.re.exception.ResourceNotFoundException;
import com.re.mapper.UserMapper;
import com.re.repository.UserRepository;
import com.re.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDetailsDto> getUsers() {
        List<User> entities = userRepository.findAll();
        return userMapper.toUserDetailsDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserProfileDto)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));
    }

    @Override
    @Transactional
    public UserUpdateResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể cập nhật. User ID " + id + " không tồn tại"));

        user.setFullName(userUpdateRequest.getFullName());
        user.setEmail(userUpdateRequest.getEmail());
        user.setPhone(userUpdateRequest.getPhone());
        user.setAddress(userUpdateRequest.getAddress());

        User savedUser = userRepository.save(user);
        return userMapper.toUserUpdateResponse(savedUser);
    }

    @Override
    @Transactional
    public void removeRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        // 2. Tìm và gỡ Role khỏi Set userRoles
        // Vì orphanRemoval = true và @OneToMany, Hibernate sẽ tự DELETE bản ghi trong bảng trung gian
        boolean removed = user.getUserRoles().removeIf(ur -> ur.getRole().getId().equals(roleId));

        if (!removed) {
            throw new BadRequestException("Người dùng không có vai trò (Role ID: " + roleId + ") này");
        }

        // 3. Lưu và trả về thông tin mới nhất (bao gồm danh sách roles đã cập nhật)
        User updatedUser = userRepository.save(user);
        userMapper.toUserUpdateResponse(updatedUser);
    }
}
