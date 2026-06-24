package com.uit.se104.feedback_system.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateManagerRequest {
    private String email;
    private String password;
    private String name;
    private String department;
    private String managementLevel;
    private String phoneNumber;
}
