package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.entity.Feedback;
import com.uit.se104.feedback_system.entity.Customer;
import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;
import com.uit.se104.feedback_system.exception.BadRequestException;
import com.uit.se104.feedback_system.exception.ResourceNotFoundException;
import com.uit.se104.feedback_system.repository.FeedbackRepository;
import com.uit.se104.feedback_system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Feedback> getCustomerFeedbackHistory(Long customerId) {
        return feedbackRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Transactional(readOnly = true)
    public List<Feedback> getCustomerFeedbackByStatus(Long customerId, FeedbackStatus status) {
        return feedbackRepository.findByCustomerIdAndStatusOrderByCreatedAtDesc(customerId, status);
    }

    @Transactional
    public Feedback submitFeedback(Long customerId, String content, Integer rating, FeedbackTopic topic) {
        // BR-07: Mandatory Feedback Content
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Feedback content must not be empty.");
        }

        // BR-08: Rating Requirement
        if (rating == null || rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Feedback feedback = new Feedback();
        feedback.setCustomer(customer);
        feedback.setContent(content);
        feedback.setRating(rating);
        // BR-12: Feedback Classification - Default if null or assigned
        feedback.setTopic(topic != null ? topic : FeedbackTopic.OTHER);
        // BR-11: Initial Feedback Status is PENDING
        feedback.setStatus(FeedbackStatus.PENDING);

        return feedbackRepository.save(feedback);
    }

    @Transactional
    public Feedback updateStatus(Long feedbackId, FeedbackStatus status) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));

        feedback.setStatus(status);
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public Feedback categorizeFeedback(Long feedbackId, FeedbackTopic topic) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));

        feedback.setTopic(topic);
        return feedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
    }
}
