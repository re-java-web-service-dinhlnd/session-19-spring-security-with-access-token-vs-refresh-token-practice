package com.re.service;

import com.re.dto.request.LoginRequest;
import com.re.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse handleLogin(LoginRequest loginRequest);
}
