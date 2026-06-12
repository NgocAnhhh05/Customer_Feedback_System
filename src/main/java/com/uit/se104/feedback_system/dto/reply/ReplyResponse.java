package com.uit.se104.feedback_system.dto.reply;

import java.time.LocalDateTime;

public record ReplyResponse(
    String replyId,
    String feedbackId,
    String adminId,
    String adminName,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
