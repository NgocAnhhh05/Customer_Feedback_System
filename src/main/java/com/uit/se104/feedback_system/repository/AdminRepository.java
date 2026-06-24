package com.uit.se104.feedback_system.repository;

import com.uit.se104.feedback_system.entity.Admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    List<Admin> findByWorkingStatus(Boolean status);

}
