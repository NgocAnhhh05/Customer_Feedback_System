package com.uit.se104.feedback_system.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.uit.se104.feedback_system.dto.feedback.*;
import com.uit.se104.feedback_system.entity.enums.*;
import com.uit.se104.feedback_system.service.FeedbackService;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService){
        this.feedbackService = feedbackService;
    }

    /*
     - API Khách hàng gửi một phản hồi đánh giá mới (UC02)
     - Nhận ID khách hàng từ Custom Header "X-Customer-Id"
     - POST http://localhost:8080/api/feedbacks
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(
        @RequestPart("feedback") @Valid FeedbackCreateRequest request, // Json object feedback
        @RequestPart(value = "files", required = false) MultipartFile[] files, // attached files
        @RequestHeader("X-Customer-Id") String customerId // Chuẩn hóa tên Header
    ){
        FeedbackResponse feedback = feedbackService.createFeedback(request, customerId, files);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    /*
     - API Khách hàng theo dõi lịch sử các phản hồi cũ và trạng thái xử lý (UC04)
     - Cho phép lọc tùy chọn theo trạng thái: PENDING, IN_PROGRESS, RESOLVED
     - GET http://localhost:8080/api/feedbacks/history
     */
    @GetMapping("/history")
    public ResponseEntity<List<FeedbackResponse>> getCustomerHistory(
        @RequestHeader("X-Customer-Id") String customerId, // Chuẩn hóa tên Header
        @RequestParam(required = false) FeedbackStatus status
    ){
        List<FeedbackResponse> responses = feedbackService.getCustomerFeedbackHistory(customerId, status);
        return ResponseEntity.ok(responses);
    }

    /*
     - API Admin/Manager duyệt xem danh sách phản hồi từ khách hàng hoặc tìm kiếm theo từ khóa (UC05)
     - GET http://localhost:8080/api/feedbacks?keyword=khiếu nại
     */
    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedbacks(
        @RequestParam(required = false) String keyword
    ){
        List<FeedbackResponse> responses = feedbackService.getAllFeedbacks(keyword);
        return ResponseEntity.ok(responses);
    }

    // Note nghiệp vụ:
    // 1. Phân loại phản hồi do khách hàng tự chọn khi tạo -> không tách riêng thành 1 api độc lập.
    // 2. Trạng thái phản hồi (Status) tự động cập nhật sang IN_PROGRESS/RESOLVED sau khi có phản hồi hệ thống -> xử lý ngầm ở tầng Service của Reply.
}