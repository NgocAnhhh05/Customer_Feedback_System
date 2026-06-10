package com.uit.se104.feedback_system.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


import com.uit.se104.feedback_system.entity.enums.ManagementLevel;
import com.uit.se104.feedback_system.entity.enums.ManagerDepartment;

@Entity
@Table(name = "manager")
@PrimaryKeyJoinColumn(name = "user_id")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Manager extends User {
    @Column(name = "management_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ManagementLevel managementLevel;

    @Column(name = "manage_department", nullable = false)
    @Enumerated(EnumType.STRING)
    private ManagerDepartment managerDepartment;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Report> generatedReport = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Admin> listOfManagedAdmin = new ArrayList<>();
}
