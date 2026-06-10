package com.uit.se104.feedback_system.repository;
import com.uit.se104.feedback_system.entity.Report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByManager_UserIdOrderByCreatedAtDesc(String managerId);

}