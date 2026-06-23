package com;

import com.uit.se104.feedback_system.controller.AuthController; // 🛑 BỔ SUNG DÒNG NÀY
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se104.feedback_system.dto.auth.*;
import com.uit.se104.feedback_system.dto.user.UserResponse;
import com.uit.se104.feedback_system.entity.enums.RoleType;
import com.uit.se104.feedback_system.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@SuppressWarnings("null")
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- HÀM HELPER TỐI ƯU HÓA CODE ---
    private static UserResponse createMockUser() {
        return new UserResponse("MGR001", "root_manager", "manager.system@uit.edu.vn", RoleType.MANAGER);
    }

    // =========================================================================
    // KỊCH BẢN 1: ĐĂNG NHẬP THÀNH CÔNG
    // =========================================================================
    @Test
    public void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("manager@cfs.com", "password123");
        AuthResponse response = new AuthResponse("mocked-jwt-token", "mocked-refresh-token", createMockUser());

        Mockito.when(authService.login(Mockito.any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.userResponse.role").value("MANAGER"));
    }

    // =========================================================================
    // KỊCH BẢN 2: THẤT BẠI DO SAI TÀI KHOẢN / MẬT KHẨU (BUSINESS LOGIC ERROR)
    // =========================================================================
    @Test
    public void testLogin_Failure_WithWrongCredentials() throws Exception {
        LoginRequest request = new LoginRequest("manager@cfs.com", "wrong_password");

        // Giả lập Service ném ra RuntimeException (hoặc BadCredentialsException tùy dự án của bạn)
        Mockito.when(authService.login(Mockito.any(LoginRequest.class)))
               .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Nếu dự án có ControllerAdvice xử lý Exception trả về 401, hãy đổi thành isUnauthorized()
                .andExpect(status().isInternalServerError()); 
    }

    // =========================================================================
    // KỊCH BẢN 3: LỖI VALIDATION - THIẾU EMAIL
    // =========================================================================
    @Test
    public void testLogin_BadRequest_WhenMissingEmail() throws Exception {
        LoginRequest request = new LoginRequest("", "password123"); // Email trống

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Kích hoạt @Valid trả về 400
    }

    // =========================================================================
    // KỊCH BẢN 4: LỖI VALIDATION - THIẾU MẬT KHẨU
    // =========================================================================
    @Test
    public void testLogin_BadRequest_WhenMissingPassword() throws Exception {
        LoginRequest request = new LoginRequest("manager@cfs.com", ""); // Mật khẩu trống

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Kích hoạt @Valid trả về 400
    }

    // =========================================================================
    // KỊCH BẢN 5: LỖI VALIDATION - EMAIL SAI ĐỊNH DẠNG
    // =========================================================================
    @Test
    public void testLogin_BadRequest_WhenInvalidEmailFormat() throws Exception {
        LoginRequest request = new LoginRequest("manager.cfs.com", "password123"); // Thiếu chữ @

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Kích hoạt @Email trả về 400
    }
}
