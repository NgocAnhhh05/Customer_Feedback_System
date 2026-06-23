package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.dto.auth.AuthResponse;
import com.uit.se104.feedback_system.dto.auth.LoginRequest;
import com.uit.se104.feedback_system.dto.auth.RegisterRequest;
import com.uit.se104.feedback_system.entity.Customer;
import com.uit.se104.feedback_system.entity.User;
import com.uit.se104.feedback_system.entity.enums.RoleType;
import com.uit.se104.feedback_system.exception.BadRequestException;
import com.uit.se104.feedback_system.exception.UnauthorizedException;
import com.uit.se104.feedback_system.repository.UserRepository;
import com.uit.se104.feedback_system.repository.CustomerRepository;
import com.uit.se104.feedback_system.security.CustomUserDetailsService;
import com.uit.se104.feedback_system.security.JwtService;
import com.uit.se104.feedback_system.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Customer:
//     self-register

// Admin:
//     manager creates

// Manager:
//     db creates

@Service
@RequiredArgsConstructor // Tự động tạo Constructor cho các final fields
@SuppressWarnings("null") //Tắt toàn bộ cảnh báo Null Type Safety cho cả file service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email().trim())) {
            throw new BadRequestException("Email already exists!");
        }

        Customer customer = Customer.builder()
                .userId(UUID.randomUUID().toString().replace("-",""))
                .name(request.name().trim())
                .email(request.email().trim().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .role(RoleType.CUSTOMER)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        // Sau khi lưu xong, nạp UserDetails để tạo Token thật
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedCustomer.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, "Bearer", EntityMapper.toUserResponse(savedCustomer));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            // 1: AuthenticationManager xác thực (nó tự gọi CustomUserDetailsService)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email().trim(),
                    request.password()
                )
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid username/email or password!");
        }

        // 2: Nếu đi đến đây tức là đã đăng nhập thành công, lấy thông tin user để tạo Token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email().trim());
        String token = jwtService.generateToken(userDetails);

        // 3: Tìm Entity User để trả về thông tin cho Frontend hiển thị (tên, email...)
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("User data not found!"));

        return new AuthResponse(token, "Bearer", EntityMapper.toUserResponse(user));
    }
}
