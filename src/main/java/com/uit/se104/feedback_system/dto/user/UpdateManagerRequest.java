package com.uit.se104.feedback_system.dto.user;

import com.uit.se104.feedback_system.entity.enums.ManagementLevel;
import com.uit.se104.feedback_system.entity.enums.ManagerDepartment;

public record UpdateManagerRequest(

        String name,

        ManagementLevel managementLevel,

        ManagerDepartment managerDepartment

) {}