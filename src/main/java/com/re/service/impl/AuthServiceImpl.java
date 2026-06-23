package com.re.service.impl;

import com.re.dto.request.LoginRequest;
import com.re.dto.response.LoginResponse;
import com.re.security.TokenProvider;
import com.re.service.AuthService;
import lombok.RequiredArgsConstructor;
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

        return null;
    }
}
