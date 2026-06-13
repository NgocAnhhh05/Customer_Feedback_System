package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.dto.user.AdminResponse;
import com.uit.se104.feedback_system.dto.user.CreateAdminRequest;
import com.uit.se104.feedback_system.dto.user.UpdateAdminRequest;
import com.uit.se104.feedback_system.dto.user.UserResponse;
import com.uit.se104.feedback_system.service.UserService;
import com.uit.se104.feedback_system.exception.BadRequestException;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")

public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    /*
     - API cho phép MANAGER tạo tài khoản ADMIN mới
     - POST http://localhost:8080/api/admins
     - Cần cấu hình Security: Chỉ cho phép ROLE_MANAGER truy cập endpoint này
     */

    @PostMapping("/admins")
    public ResponseEntity<AdminResponse> createAdminByManager(@Valid @RequestBody CreateAdminRequest request) throws BadRequestException{
        AdminResponse response = userService.createAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
     - API Quản lý truy vấn danh sách toàn bộ nhân viên vận hành (UC10)
     - GET http://localhost:8080/api/users/admins
     */
    @GetMapping("/admins")
    public ResponseEntity<List<AdminResponse>> getAdminList() {
        List<AdminResponse> response = userService.getAdminList();
        return ResponseEntity.ok(response);
    }

    /*
     - API Quản lý tạm ngưng/bật lại kích hoạt tài khoản của một nhân viên (UC10 - Alt Flow)
     - PUT http://localhost:8080/api/users/admins/{id}/toggle?active=false
     */

     @PutMapping("/admins/{adminId}/toggle")
     public ResponseEntity<UserResponse> toggleAdminStatus(@PathVariable String adminId, @RequestParam boolean active){
        UserResponse response = userService.toggleAdminWorkingStatus(adminId, active);
        return ResponseEntity.ok(response);
     }

     // admin self-update his info
     @PutMapping("/admins/{adminId}")
     public ResponseEntity<AdminResponse> updateAdmin(
            @PathVariable String adminId,
            @Valid @RequestBody UpdateAdminRequest request) {
        AdminResponse responses = userService.updateAdmin(adminId, request);
        return ResponseEntity.ok(responses);
    }


}
