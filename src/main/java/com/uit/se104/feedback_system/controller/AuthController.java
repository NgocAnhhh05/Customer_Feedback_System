package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.dto.auth.*;
import com.uit.se104.feedback_system.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*
AuthController: Đăng nhập/Đăng ký.

UserController: Quản lý danh sách nhân viên (Admin).

FeedbackController: Khách hàng gửi và xem lịch sử feedback, Admin tìm kiếm.

ReplyController: Admin phản hồi feedback.

ReportController: Manager xem Dashboard và xuất file báo cáo.
*/
// Controller tiếp nhận các yêu cầu liên quan đến Xác thực tài khoản
// login for customer/ admin/ manager
// register just for customer

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

     /*
     - API tiếp nhận yêu cầu Đăng ký tài khoản Khách hàng mới (UC01)
     - POST http://localhost:8080/api/auth/register
     */
    // Khách hàng tự đăng ký tài khoản (Public API)
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
     - API tiếp nhận yêu cầu Đăng nhập hệ thống (UC01)
     - POST http://localhost:8080/api/auth/login
     */
    // Đăng nhập chung cho tất cả các Role: Customer, Manager, Admin (Public API)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
