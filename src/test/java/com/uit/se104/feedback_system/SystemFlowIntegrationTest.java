package com.uit.se104.feedback_system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se104.feedback_system.dto.auth.*;
import com.uit.se104.feedback_system.dto.user.*;
import com.uit.se104.feedback_system.dto.feedback.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String managerToken;
    private static String adminToken;
    private static String customerToken;

    @Test
    @Order(1)
    public void step1_ManagerLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest("manager@cfs.com", "manager_password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        AuthResponse response = objectMapper.readValue(responseStr, AuthResponse.class);
        managerToken = response.getToken();
    }

    @Test
    @Order(2)
    public void step2_ManagerCreatesAdmin_Success() throws Exception {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("admin_new@cfs.com", "admin_password");

        mockMvc.perform(post("/api/users/admins")
                .header("Authorization", "Bearer " + managerToken) // Mô phỏng JWT filter kiểm tra quyền
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAdminRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    public void step3_CustomerRegister_Success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("customer_new@gmail.com", "customer_password");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        AuthResponse response = objectMapper.readValue(responseStr, AuthResponse.class);
        customerToken = response.getToken();
    }

    @Test
    @Order(4)
    public void step4_CustomerSubmitsFeedbackWithFiles_Success() throws Exception {
        // Tạo JSON Data cho Feedback
        MockMultipartFile feedbackJsonPart = new MockMultipartFile(
                "feedback", "", "application/json",
                "{\"content\":\"Hệ thống chạy mượt mà, đính kèm ảnh lỗi UI.\",\"topic\":\"UI_UX\",\"rating\":4}".getBytes()
        );

        // Tạo tệp tin thực tế giả lập (Ảnh/PDF)
        MockMultipartFile mockImage = new MockMultipartFile(
                "files", "screenshot.png", "image/png", "fake-image-content".getBytes()
        );
        MockMultipartFile mockPdf = new MockMultipartFile(
                "files", "document.pdf", "application/pdf", "fake-pdf-content".getBytes()
        );

        mockMvc.perform(multipart("/api/feedbacks")
                .file(feedbackJsonPart)
                .file(mockImage)
                .file(mockPdf)
                .header("X-Customer-Id", "CUST-1002") // Nhận ID từ header theo cấu hình Controller
                .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    @Order(5)
    public void testException_401_Unauthorized_WhenWrongRole() throws Exception {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("hacker@cfs.com", "123");
        
        // Dùng token của CUSTOMER để gọi API tạo ADMIN dành riêng cho MANAGER
        mockMvc.perform(post("/api/users/admins")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAdminRequest)))
                .andExpect(status().isUnauthorized()); // Trả về 401 khi kiểm tra Role bị từ chối
    }
}