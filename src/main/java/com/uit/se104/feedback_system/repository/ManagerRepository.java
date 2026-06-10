package com.uit.se104.feedback_system.repository;
import com.uit.se104.feedback_system.entity.Manager;
import com.uit.se104.feedback_system.entity.enums.ManagerDepartment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {
    List<Manager> findByManagerDepartment(ManagerDepartment managerDepartment);

}