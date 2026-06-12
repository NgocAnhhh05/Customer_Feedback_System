package com.uit.se104.feedback_system.dto.user;
import com.uit.se104.feedback_system.entity.enums.AdminSpecialization;

public record AdminResponse(

        String userId,

        String name,

        String email,

        AdminSpecialization specialization,

        Boolean workingStatus

) {}
