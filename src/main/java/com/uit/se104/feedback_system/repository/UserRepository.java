package com.uit.se104.feedback_system.repository;

import com.uit.se104.feedback_system.entity.User;
import com.uit.se104.feedback_system.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByNameIgnoreCase(String nam);

    boolean existsByEmailIgnoreCase(String email);
    boolean existsBynameIgnoreCase(String username);

    List<User> findAllByRole(RoleType role);

    List<User> findByRole(RoleType role);
    long countByRole(RoleType role);
}
