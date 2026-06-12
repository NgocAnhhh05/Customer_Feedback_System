package com.uit.se104.feedback_system.dto.user;
import com.uit.se104.feedback_system.entity.enums.AdminSpecialization;

// when admin update info
public record UpdateAdminRequest(

        String name,
        AdminSpecialization specialization,

        Boolean workingStatus

) {}