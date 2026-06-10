package com.uit.se104.feedback_system.repository;

import com.uit.se104.feedback_system.entity.Customer;
import com.uit.se104.feedback_system.entity.enums.RoleType;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser() {

        Customer customer = Customer.builder()
                .userId("C001")
                .name("Ngoc Anh")
                .email("ngocanh@gmail.com")
                .password("123")
                .role(RoleType.CUSTOMER)
                .build();

        userRepository.save(customer);
    }
}