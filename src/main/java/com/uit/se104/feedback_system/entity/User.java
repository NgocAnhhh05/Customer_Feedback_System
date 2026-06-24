package com.uit.se104.feedback_system.entity;

import com.uit.se104.feedback_system.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "role_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "phone_number")
    private String phoneNumber;
}
