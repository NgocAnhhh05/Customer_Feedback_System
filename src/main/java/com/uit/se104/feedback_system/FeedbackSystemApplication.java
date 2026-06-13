package com.uit.se104.feedback_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uit.se104.feedback_system.entity.enums.RoleType;
import com.uit.se104.feedback_system.repository.ManagerRepository;
import com.uit.se104.feedback_system.entity.Manager;

@SpringBootApplication
public class FeedbackSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedbackSystemApplication.class, args);
	}

	// Tự động kiểm tra và tạo 1 tài khoản Manager trong DB nếu chưa có khi ứng dụng chạy lên
    @Bean
    CommandLineRunner initManagerAccount(ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (managerRepository.count() == 0) {
                Manager defaultManager = Manager.builder()
                        .userId("MGR001")
                        .name("root_manager")
                        .password(passwordEncoder.encode("ManagerPassword123!"))
                        .email("manager.system@uit.edu.vn")
                        .role(RoleType.MANAGER) 
                        .build();
                managerRepository.save(defaultManager);
                System.out.println(">>> Default Manager account created: root_manager / ManagerPassword123!");
            }
        };
    }

}
