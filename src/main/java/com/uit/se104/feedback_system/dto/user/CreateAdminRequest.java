package com.uit.se104.feedback_system.dto.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.uit.se104.feedback_system.entity.enums.AdminSpecialization;

// use when manager create new admin
public record CreateAdminRequest(

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50)
    String name,

    @NotBlank(message = "Email cannot be blank")
    @Email
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 50)
    String password,

    AdminSpecialization specialization

) {}