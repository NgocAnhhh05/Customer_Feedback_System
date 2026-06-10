package com.uit.se104.feedback_system.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


import com.uit.se104.feedback_system.entity.enums.AdminSpecialization;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Admin extends User {
    @Column(name = "specialization")
    private AdminSpecialization specialization;

    @Column(name = "working_status")
    private boolean workingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reply> listOfSentReply = new ArrayList<>();
}
