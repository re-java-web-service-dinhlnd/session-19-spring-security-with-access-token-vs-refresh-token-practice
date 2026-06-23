package com.re.service;

import com.re.dto.UserDetailsDto;
import com.re.dto.UserProfileDto;
import com.re.dto.request.UserUpdateRequest;
import com.re.dto.response.UserUpdateResponse;

import java.util.List;

public interface UserService {
    List<UserDetailsDto> getUsers();
    UserProfileDto getUser(Long id);
    UserUpdateResponse updateUser(Long id, UserUpdateRequest userUpdateRequest);
    void removeRole(Long userId, Long roleId);
}
