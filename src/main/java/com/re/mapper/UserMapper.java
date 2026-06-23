package com.re.mapper;

import com.re.dto.UserDetailsDto;
import com.re.dto.UserLoginDto;
import com.re.dto.UserProfileDto;
import com.re.dto.response.UserUpdateResponse;
import com.re.entity.User;

import java.util.List;

public interface UserMapper {
    UserLoginDto toUserLogin(Object userPrincipal);
    UserDetailsDto toUserDetailsDto(User user);
    List<UserDetailsDto> toUserDetailsDtoList(List<User> users);
    UserProfileDto toUserProfileDto(User user);
    UserUpdateResponse toUserUpdateResponse(User user);
}
