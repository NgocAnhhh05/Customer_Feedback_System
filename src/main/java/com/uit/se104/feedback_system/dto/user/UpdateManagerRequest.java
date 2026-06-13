package com.uit.se104.feedback_system.dto.user;

import com.uit.se104.feedback_system.entity.enums.ManagementLevel;
import com.uit.se104.feedback_system.entity.enums.ManageDepartment;

public record UpdateManagerRequest(

        String name,

        ManagementLevel managementLevel,

        ManageDepartment manageDepartment

) {}