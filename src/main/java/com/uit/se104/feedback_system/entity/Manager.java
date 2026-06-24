package com.uit.se104.feedback_system.entity;

import com.uit.se104.feedback_system.entity.enums.ManageDepartment;
import com.uit.se104.feedback_system.entity.enums.ManagementLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Manager extends User {

    @Column(name = "department", nullable = false)
    @Enumerated(EnumType.STRING)
    private ManageDepartment department;

    @Column(name = "management_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ManagementLevel managementLevel;
}
