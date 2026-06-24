package com.uit.se104.feedback_system.entity;

import com.uit.se104.feedback_system.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public abstract class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;
}