package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.dto.auth.AuthResponse;
import com.uit.se104.feedback_system.dto.auth.LoginRequest;
import com.uit.se104.feedback_system.dto.auth.RegisterRequest;
import com.uit.se104.feedback_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
