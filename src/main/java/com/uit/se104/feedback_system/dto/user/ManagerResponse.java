package com.uit.se104.feedback_system.dto.user;
import com.uit.se104.feedback_system.entity.enums.ManagementLevel;
import com.uit.se104.feedback_system.entity.enums.ManageDepartment;

public record ManagerResponse(

        String userId,

        String name,

        String email,

        ManagementLevel managementLevel,

        ManageDepartment manageDepartment

) {}