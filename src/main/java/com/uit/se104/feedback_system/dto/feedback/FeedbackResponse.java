package com.uit.se104.feedback_system.dto.feedback;
import com.uit.se104.feedback_system.dto.reply.ReplyResponse;
import com.uit.se104.feedback_system.dto.feedback.AttachmentResponse;
import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;
import java.time.LocalDateTime;
import java.util.List;

public record FeedbackResponse(
    String feedbackId,
    String customerId,
    String customerName,
    String content,
    FeedbackTopic topic,
    Integer rating,
    FeedbackStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<AttachmentResponse> attachments,
    List<ReplyResponse> replies
) {
}