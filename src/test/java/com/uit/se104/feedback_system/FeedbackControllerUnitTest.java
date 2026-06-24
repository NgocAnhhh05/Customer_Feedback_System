package com;

import com.uit.se104.feedback_system.controller.FeedbackController; // 🛑 BỔ SUNG DÒNG NÀY
import com.uit.se104.feedback_system.service.FeedbackService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // 🛑 Dùng thư viện mới thay cho MockBean
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 🛑 Giải pháp chống xung đột: Loại bỏ lớp cấu hình SecurityAutoConfiguration để cô lập Context test
@WebMvcTest(value = FeedbackController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})

public class FeedbackControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // 🛑 Đổi sang @MockitoBean theo chuẩn Spring Boot 3.5+
    private FeedbackService feedbackService;

    // =========================================================================
    // KỊCH BẢN 1: THẤT BẠI DO THIẾU HEADER ĐỊNH DANH (TEST CASE GỐC)
    // =========================================================================
    @Test
    public void testCreateFeedback_BadRequest_WhenMissingCustomerHeader() throws Exception {
        MockMultipartFile feedbackJson = new MockMultipartFile("feedback", "", "application/json", 
                "{\"content\":\"Hàng tốt\",\"topic\":\"PRODUCT\",\"rating\":5}".getBytes());

        // Cố tình không truyền header "X-Customer-Id"
        mockMvc.perform(multipart("/api/feedbacks")
                .file(feedbackJson))
                .andExpect(status().isBadRequest()); // Trả về lỗi 400
    }

    // =========================================================================
    // KỊCH BẢN 2: ĐĂNG PHẢN HỒI KÈM HÌNH ẢNH THÀNH CÔNG (HAPPY PATH)
    // =========================================================================
    @Test
    public void testCreateFeedback_Success_WithImage() throws Exception {
        MockMultipartFile feedbackJson = new MockMultipartFile("feedback", "", "application/json", 
                "{\"content\":\"Ứng dụng chói quá, cần Dark mode\",\"topic\":\"OTHER\",\"rating\":5}".getBytes());
        
        // Tạo file ảnh giả lập dạng PNG gửi lên trùng khớp với tham số "files" ở giao diện Frontend
        MockMultipartFile mockImage = new MockMultipartFile("files", "test-darkmode.png", "image/png", 
                "fake-image-binary-content".getBytes());

        mockMvc.perform(multipart("/api/feedbacks")
                .file(feedbackJson)
                .file(mockImage)
                .header("X-Customer-Id", "CUST_001")) // Đính kèm header hợp lệ
                .andExpect(status().isOk()); // Hoặc isCreated() tùy thiết kế API của bạn
    }

    // =========================================================================
    // KỊCH BẢN 3: LỖI VALIDATION - NỘI DUNG PHẢN HỒI TRỐNG
    // =========================================================================
    @Test
    public void testCreateFeedback_BadRequest_WhenContentIsEmpty() throws Exception {
        // Gửi chuỗi nội dung trống "" để kích hoạt kiểm tra @NotBlank/@Valid ở Controller
        MockMultipartFile invalidFeedbackJson = new MockMultipartFile("feedback", "", "application/json", 
                "{\"content\":\"\",\"topic\":\"PRODUCT\",\"rating\":5}".getBytes());

        mockMvc.perform(multipart("/api/feedbacks")
                .file(invalidFeedbackJson)
                .header("X-Customer-Id", "CUST_001"))
                .andExpect(status().isBadRequest()); // Kích hoạt Validation trả về 400
    }

    // =========================================================================
    // KỊCH BẢN 4: LỖI VALIDATION - ĐIỂM ĐÁNH GIÁ (RATING) VƯỢT QUÁ KHUÔN KHỔ
    // =========================================================================
    @Test
    public void testCreateFeedback_BadRequest_WhenRatingIsInvalid() throws Exception {
        // Gửi số 10 vượt quá giới hạn @Max(5) của trường rating
        MockMultipartFile invalidFeedbackJson = new MockMultipartFile("feedback", "", "application/json", 
                "{\"content\":\"Giao diện tạm ổn\",\"topic\":\"PRODUCT\",\"rating\":10}".getBytes());

        mockMvc.perform(multipart("/api/feedbacks")
                .file(invalidFeedbackJson)
                .header("X-Customer-Id", "CUST_001"))
                .andExpect(status().isBadRequest()); // Trả về lỗi 400
    }
}
