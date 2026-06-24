package com.uit.se104.feedback_system.mapper;

import com.uit.se104.feedback_system.dto.feedback.FeedbackResponse;
import com.uit.se104.feedback_system.dto.user.UserResponse;
import com.uit.se104.feedback_system.entity.Feedback;
import com.uit.se104.feedback_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setRole(user.getRole().name());
        response.setActive(user.isActive());
        return response;
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback) {
        if (feedback == null) return null;
        FeedbackResponse response = new FeedbackResponse();
        response.setId(feedback.getId());
        response.setCustomerName(feedback.getCustomer().getName());
        response.setContent(feedback.getContent());
        response.setRating(feedback.getRating());
        response.setTopic(feedback.getTopic().name());
        response.setStatus(feedback.getStatus().name());
        response.setCreatedAt(feedback.getCreatedAt());
        return response;
    }
}
