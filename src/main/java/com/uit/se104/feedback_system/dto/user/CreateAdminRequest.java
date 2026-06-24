package com.uit.se104.feedback_system.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdminRequest {
    private String email;
    private String password;
    private String name;
    private String specialization;
    private String phoneNumber;
}
