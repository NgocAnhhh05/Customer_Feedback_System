package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.entity.Feedback;
import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;
import com.uit.se104.feedback_system.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @PostMapping("/submit")
    public ResponseEntity<Feedback> submitFeedback(
            @RequestParam Long customerId,
            @RequestParam String content,
            @RequestParam Integer rating,
            @RequestParam(required = false) FeedbackTopic topic) {
        return ResponseEntity.ok(feedbackService.submitFeedback(customerId, content, rating, topic));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Feedback> updateStatus(
            @PathVariable Long id,
            @RequestParam FeedbackStatus status) {
        return ResponseEntity.ok(feedbackService.updateStatus(id, status));
    }

    @PutMapping("/{id}/categorize")
    public ResponseEntity<Feedback> categorize(
            @PathVariable Long id,
            @RequestParam FeedbackTopic topic) {
        return ResponseEntity.ok(feedbackService.categorizeFeedback(id, topic));
    }
}
