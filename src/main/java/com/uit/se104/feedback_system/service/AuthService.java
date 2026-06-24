package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.dto.auth.AuthResponse;
import com.uit.se104.feedback_system.dto.auth.LoginRequest;
import com.uit.se104.feedback_system.dto.auth.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public AuthResponse login(LoginRequest request) {
        return new AuthResponse("mock-jwt-token", "Login successful", request.getEmail());
    }

    public AuthResponse register(RegisterRequest request) {
        return new AuthResponse("mock-jwt-token", "Registration successful", request.getEmail());
    }
}
