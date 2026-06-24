package com.uit.se104.feedback_system.dto.feedback;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class FeedbackResponse {
    private Long id;
    private String customerName;
    private String content;
    private Integer rating;
    private String topic;
    private String status;
    private LocalDateTime createdAt;
}
