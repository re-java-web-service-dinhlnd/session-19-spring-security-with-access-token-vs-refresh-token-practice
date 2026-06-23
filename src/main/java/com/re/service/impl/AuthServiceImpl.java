package com.re.service.impl;

import com.re.dto.UserLoginDto;
import com.re.dto.request.LoginRequest;
import com.re.dto.response.LoginResponse;
import com.re.entity.User;
import com.re.mapper.UserMapper;
import com.re.repository.UserRepository;
import com.re.security.TokenProvider;
import com.re.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Value("${}")
    private Long accessTokenExpirationMs;

    @Override
    public LoginResponse handleLogin(LoginRequest loginRequest) {
        // 1. Perform authentication through AuthenticationManager
        UsernamePasswordAuthenticationToken currentUser = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(currentUser);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate a Access Token from the authentication object
        String accessToken = tokenProvider.generateAccessToken(authentication);

        // 4. Transfer principal and map information to DTO
        Object principal = authentication.getPrincipal();
        UserLoginDto userLoginDto = userMapper.toUserLogin(principal);

        User user = userRepository.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Returns LoginResponse along with the Access Token
        return LoginResponse.builder()
                .message("Login successfully!")
                .accessToken(accessToken)
//                .refreshToken(refreshToken.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(accessTokenExpirationMs)
                .user(userLoginDto)
                .build();
    }
}
