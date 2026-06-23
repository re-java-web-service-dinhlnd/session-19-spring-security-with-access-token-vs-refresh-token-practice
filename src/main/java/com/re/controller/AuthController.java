package com.re.controller;

import com.re.dto.request.LoginRequest;
import com.re.dto.request.RegisterRequest;
import com.re.dto.response.LoginResponse;
import com.re.dto.response.RegisterResponse;
import com.re.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.handleLogin(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

//    @PostMapping("/register")
//    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
//        RegisterResponse registerResponse = authService.handleRegister(registerRequest);
//
//        // Create a URI that points to a new resource (e.g., /api/users/{id})
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/api/users/{id}")
//                .buildAndExpand(registerResponse.getUser().getId()).toUri();
//        return ResponseEntity.created(location).body(registerResponse);
//    }
}
