package com.uit.se104.feedback_system.dto.reply;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyResponse {
    private Long id;
    private Long feedbackId;
    private String adminName;
    private String content;
    private LocalDateTime createdAt;
}
