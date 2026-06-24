package com.uit.se104.feedback_system.entity;

import com.uit.se104.feedback_system.entity.enums.AdminSpecialization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Admin extends User {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdminSpecialization specialization;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();
}
