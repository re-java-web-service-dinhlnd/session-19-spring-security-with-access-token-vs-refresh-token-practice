package com.re.mapper.impl;

import com.re.dto.UserDetailsDto;
import com.re.dto.UserProfileDto;
import com.re.dto.response.UserUpdateResponse;
import com.re.entity.User;
import com.re.entity.UserRole;
import com.re.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDetailsDto toUserDetailsDto(User user) {
        if (user == null) return null;

        return UserDetailsDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(mapUserRolesToStrings(user.getUserRoles()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public List<UserDetailsDto> toUserDetailsDtoList(List<User> users) {
        if (users == null) return Collections.emptyList();
        return users.stream()
                .map(this::toUserDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDto toUserProfileDto(User user) {
        if (user == null) return null;

        return UserProfileDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserUpdateResponse toUserUpdateResponse(User user) {
        if (user == null) return null;

        return UserUpdateResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roles(mapUserRolesToStrings(user.getUserRoles()))
                .updatedAt(String.valueOf(user.getUpdatedAt()))
                .build();
    }

    /**
     * Helper method dùng chung để map Roles
     */
    private Set<String> mapUserRolesToStrings(Set<UserRole> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return Collections.emptySet();
        }
        return userRoles.stream()
                .filter(ur -> ur.getRole() != null)
                .map(ur -> ur.getRole().getName()) // Hoặc .getName().name() tùy kiểu dữ liệu
                .collect(Collectors.toSet());
    }
}
