package com.uit.se104.feedback_system.dto.feedback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackCreateRequest {
    private String content;
    private Integer rating;
    private String topic;
}
