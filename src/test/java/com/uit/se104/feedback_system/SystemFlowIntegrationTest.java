package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se104.feedback_system.dto.auth.*;
import com.uit.se104.feedback_system.dto.user.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("null") // Triệt tiêu hoàn toàn cảnh báo Null type safety cho toàn bộ chuỗi tích hợp
public class SystemFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Bộ nhớ đệm lưu Token tĩnh để truyền xuyên suốt qua từng bước (Step) kiểm thử
    private static String managerToken;
    private static String customerToken;

    // =========================================================================
    // BƯỚC 1: QUẢN LÝ ĐĂNG NHẬP (LẤY TOKEN ĐỂ ĐIỀU HÀNH)
    // =========================================================================
    @Test
    @Order(1)
    public void step1_ManagerLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest("manager@cfs.com", "manager_password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        AuthResponse response = objectMapper.readValue(responseStr, AuthResponse.class);
        
        // 🛑 ĐỒNG BỘ: Đổi từ .getToken() thành .accessToken() theo chuẩn cấu trúc Record mới
        managerToken = response.accessToken(); 
    }

    // =========================================================================
    // BƯỚC 2: QUẢN LÝ TẠO QUẢN TRỊ VIÊN (ADMIN) MỚI
    // =========================================================================
    @Test
    @Order(2)
    public void step2_ManagerCreatesAdmin_Success() throws Exception {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest(
            "Nguyễn Văn Admin", 
            "admin_new@cfs.com", 
            "admin_password", 
            null // Hoặc trường chuyên môn bất kỳ của dự án
    );
        mockMvc.perform(post("/api/users/admins")
                .header("Authorization", "Bearer " + managerToken) // Đính kèm JWT thật vừa sinh ra ở Bước 1
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createAdminRequest)))
                .andExpect(status().isCreated());
    }

    // =========================================================================
    // BƯỚC 3: KHÁCH HÀNG MỚI ĐĂNG KÝ TÀI KHOẢN TỰ ĐỘNG
    // =========================================================================
    @Test
    @Order(3)
    public void step3_CustomerRegister_Success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
            "Nguyễn Khách Hàng", 
            "customer_new@gmail.com", 
            "customer_password"
        );
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        AuthResponse response = objectMapper.readValue(responseStr, AuthResponse.class);
        
        // 🛑 ĐỒNG BỘ: Lấy accessToken của khách hàng vừa đăng ký thành công
        customerToken = response.accessToken(); 
    }

    // =========================================================================
    // BƯỚC 4: KHÁCH HÀNG GỬI PHẢN HỒI KÈM MULTIPART FILE (HÌNH ẢNH/PDF)
    // =========================================================================
    @Test
    @Order(4)
    public void step4_CustomerSubmitsFeedbackWithFiles_Success() throws Exception {
        // Khung gói tin chứa text giải lập, khớp cấu trúc DTO đầu vào của bạn
        MockMultipartFile feedbackJsonPart = new MockMultipartFile(
                "feedback", "", "application/json",
                "{\"content\":\"Hệ thống chạy mượt mà, đính kèm ảnh lỗi UI.\",\"topic\":\"UI_UX\",\"rating\":4}".getBytes()
        );

        // Tạo dữ liệu nhị phân giả lập cho tệp tin, khớp chính xác tham số "files" của FormData Frontend
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
                .header("X-Customer-Id", "CUST-1002") // Header tùy chỉnh nhận diện ID khách hàng
                .header("Authorization", "Bearer " + customerToken)) // Token phân quyền CUSTOMER hợp lệ
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").exists());
    }

    // =========================================================================
    // BƯỚC 5: KIỂM TRA BẮT EXCEPTION PHÂN QUYỀN (ROLE-BASED ERROR)
    // =========================================================================
    @Test
    @Order(5)
    public void testException_403_Forbidden_WhenWrongRole() throws Exception {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest(
            "Hacker Admin", 
            "hacker@cfs.com", 
            "123456", // Đảm bảo độ dài từ 6 ký tự trở lên để vượt qua bộ lọc @Size(min = 6)
            null
        );
        
        // DÙNG TOKEN KHÁCH HÀNG để cố tình gọi API tạo ADMIN của QUẢN LÝ (MANAGER)
        mockMvc.perform(post("/api/users/admins")
                .header("Authorization", "Bearer " + customerToken)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createAdminRequest)))
                // 🛑 SỬA TẠI ĐÂY: Theo chuẩn Spring Security, sai quyền hạn (Role) hệ thống sẽ ném về 403 Forbidden
                // (Chỉ ném về 401 Unauthorized khi token bị trống hoặc không hợp lệ hoàn toàn)
                .andExpect(status().isForbidden()); 
    }
}
