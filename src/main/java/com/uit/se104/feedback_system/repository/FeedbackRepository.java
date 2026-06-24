package com.uit.se104.feedback_system.repository;

import com.uit.se104.feedback_system.entity.Feedback;
import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Feedback> findByCustomerIdAndStatusOrderByCreatedAtDesc(Long customerId, FeedbackStatus status);
    List<Feedback> findAllByOrderByCreatedAtDesc();
}
