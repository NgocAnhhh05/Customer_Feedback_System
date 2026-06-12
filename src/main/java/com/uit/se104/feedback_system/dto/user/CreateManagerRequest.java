package com.uit.se104.feedback_system.dto.user;

import com.uit.se104.feedback_system.entity.enums.ManagementLevel;
import com.uit.se104.feedback_system.entity.enums.ManagerDepartment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateManagerRequest(

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 100)
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100)
        String password,

        @NotNull(message = "Management level is required")
        ManagementLevel managementLevel,

        @NotNull(message = "Department is required")
        ManagerDepartment managerDepartment

) {}