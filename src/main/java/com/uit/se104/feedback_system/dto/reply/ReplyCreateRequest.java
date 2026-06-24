package com.uit.se104.feedback_system.dto.reply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyCreateRequest {
    private Long feedbackId;
    private String content;
}
