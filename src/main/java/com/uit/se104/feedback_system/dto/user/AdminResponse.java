package com.uit.se104.feedback_system.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponse {
    private Long id;
    private String email;
    private String name;
    private String specialization;
    private boolean isActive;
}
