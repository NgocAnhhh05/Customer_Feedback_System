package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.dto.reply.*;
import com.uit.se104.feedback_system.service.ReplyService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/feedbacks/{feedbackId}/replies")
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService){
        this.replyService = replyService;
    }

    /*
     - API Admin/Manager viết câu trả lời cho một phản hồi của khách hàng (UC07)
     - POST http://localhost:8080/api/feedbacks/{feedbackId}/replies
     - Note nghiệp vụ: Trạng thái của Feedback sẽ tự động chuyển sang RESOLVED/IN_PROGRESS ngầm trong ReplyService.
     */
    @PostMapping
    public ResponseEntity<ReplyResponse> createReply(
        @PathVariable String feedbackId,
        @Valid @RequestBody ReplyCreateRequest request,
        @RequestHeader("X-Admin-Id") String adminId
    ){
        ReplyResponse response = replyService.createReply(feedbackId, request, adminId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
     - API Xem danh sách các câu trả lời/phản hồi của một Feedback cụ thể
     - Cả Khách hàng (để đọc câu trả lời) và Admin (để xem lịch sử trao đổi) đều cần dùng API này
     - GET http://localhost:8080/api/feedbacks/{feedbackId}/replies
     */
    @GetMapping
    public ResponseEntity<List<ReplyResponse>> getRepliesByFeedbackId(@PathVariable String feedbackId) {
        List<ReplyResponse> responses = replyService.getRepliesByFeedbackId(feedbackId);
        return ResponseEntity.ok(responses);
    }

}
