package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.entity.Manager;
import com.uit.se104.feedback_system.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }
}
