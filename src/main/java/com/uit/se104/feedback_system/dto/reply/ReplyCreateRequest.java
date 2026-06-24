package com.uit.se104.feedback_system.dto.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReplyCreateRequest (
    @NotBlank(message = "Reply content cannot be blank")
    @Size(min = 5, message = "Reply content must be at least 5 characters")
    String content
)
{}
