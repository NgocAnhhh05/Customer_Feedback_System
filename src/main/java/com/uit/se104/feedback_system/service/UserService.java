package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.dto.user.CreateAdminRequest;
import com.uit.se104.feedback_system.dto.user.AdminResponse;
import com.uit.se104.feedback_system.dto.user.UpdateAdminRequest;
import com.uit.se104.feedback_system.dto.user.UserResponse;
import com.uit.se104.feedback_system.exception.*;
import com.uit.se104.feedback_system.entity.Admin;
import com.uit.se104.feedback_system.entity.enums.*;
import com.uit.se104.feedback_system.mapper.EntityMapper;
import com.uit.se104.feedback_system.repository.AdminRepository;
import com.uit.se104.feedback_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.uit.se104.feedback_system.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// Customer:
//     self-register

// Admin:
//     manager creates

// Manager:
//     db inserted

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class UserService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // admin management by manager
    @Transactional
    public AdminResponse createAdmin(CreateAdminRequest request) throws BadRequestException{
        if (userRepository.existsByEmailIgnoreCase(request.email().trim())){
            throw new BadRequestException("This email is already registered");
        }

        Admin admin = Admin.builder()
            .userId(UUID.randomUUID().toString().replace("-", ""))
            .name(request.name().trim())
            .email(request.email().trim().toLowerCase())
            .password(passwordEncoder.encode(request.password()))
            .role(RoleType.ADMIN)
            .specialization(request.specialization())
            .workingStatus(true)
            .build();
        Admin savedAdmin = adminRepository.save(admin);
        return EntityMapper.toAdminResponse(savedAdmin);
    }

    @Transactional
    public AdminResponse updateAdmin(String adminId, UpdateAdminRequest request){
        Admin admin = adminRepository.findById(adminId)
                                     .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        admin.setName(request.name().trim());
        admin.setSpecialization(request.specialization());
        return EntityMapper.toAdminResponse(admin);
    }

    @Transactional(readOnly = true)
    public List<AdminResponse> getAdminList(){
        return adminRepository.findAll().stream()
                                        .map(EntityMapper::toAdminResponse)
                                        .toList();
    }

    @Transactional
    public UserResponse toggleAdminWorkingStatus(String adminId, boolean active){
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("Admin member not found with ID: " + adminId));

        admin.setWorkingStatus(active);
        return EntityMapper.toUserResponse(admin);
    }

}

