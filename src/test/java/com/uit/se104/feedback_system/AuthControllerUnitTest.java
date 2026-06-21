package com.uit.se104.feedback_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se104.feedback_system.dto.auth.*;
import com.uit.se104.feedback_system.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("manager@cfs.com", "password123");
        AuthResponse response = new AuthResponse("mocked-jwt-token", "Manager");

        Mockito.when(authService.login(Mockito.any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.role").value("Manager"));
    }

    @Test
    public void testLogin_BadRequest_WhenMissingEmail() throws Exception {
        LoginRequest request = new LoginRequest("", "password123"); // Trống email kích hoạt @Valid

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Trả về lỗi 400 Validation
    }
}