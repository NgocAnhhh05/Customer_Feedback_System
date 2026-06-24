package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.dto.user.ManagerResponse;
import com.uit.se104.feedback_system.dto.user.UpdateManagerRequest;
import com.uit.se104.feedback_system.entity.Manager;
import com.uit.se104.feedback_system.exception.ResourceNotFoundException;
import com.uit.se104.feedback_system.mapper.EntityMapper;
import com.uit.se104.feedback_system.repository.ManagerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ManagerService {

    private final ManagerRepository managerRepository;

    @Transactional(readOnly = true)
    public ManagerResponse getManager(
            String managerId
    ) {

        Manager manager =
                managerRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                )
                        );

        return EntityMapper.toManagerResponse(
                manager
        );
    }

    @Transactional
    public ManagerResponse updateManager(
            String managerId,
            UpdateManagerRequest request
    ) {

        Manager manager =
                managerRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                )
                        );

        manager.setManagementLevel(
                request.managementLevel()
        );

        manager.setManageDepartment(
                request.manageDepartment()
        );

        return EntityMapper.toManagerResponse(
                manager
        );
    }
}
