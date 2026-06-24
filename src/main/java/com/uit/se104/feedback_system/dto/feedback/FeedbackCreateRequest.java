package com.uit.se104.feedback_system.dto.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;

public record FeedbackCreateRequest(
    @NotBlank(message = "Feedback content must not be blank")
    @Size(min = 10, message = "Feedback content must be at least 10 characters long")
    String content,

    @NotNull(message = "Please select a feedback topic")
    FeedbackTopic topic,

    @Min(value = 1, message = "Rating must be at least 1 star")
    @Max(value = 5, message = "Rating cannot exceed 5 stars")
    int rating

) {
}
