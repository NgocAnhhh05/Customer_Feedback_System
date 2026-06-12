package com.uit.se104.feedback_system.dto.user;
import com.uit.se104.feedback_system.entity.enums.RoleType;

// Admin -> AdminResponse
// Manager -> ManagerResponse
// Customer -> UserResponse

public record UserResponse(
    String userId,
    String name,
    String email,
    RoleType role
) {
}
