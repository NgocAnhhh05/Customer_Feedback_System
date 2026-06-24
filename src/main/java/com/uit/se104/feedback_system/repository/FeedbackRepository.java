package com.uit.se104.feedback_system.repository;
import com.uit.se104.feedback_system.entity.Feedback;
import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository
        extends JpaRepository<Feedback, String> {

    List<Feedback> findAllByCustomer_UserIdOrderByCreatedAtDesc(
            String customerId
    );

    List<Feedback> findAllByCustomer_UserIdAndStatusOrderByCreatedAtDesc(
            String customerId,
            FeedbackStatus status
    );

    List<Feedback> findAllByOrderByCreatedAtDesc();

    List<Feedback> findByContentContainingIgnoreCase(String keyword);

    List<Feedback> findAllByStatus(FeedbackStatus status);

    List<Feedback> findAllByTopic(FeedbackTopic topic);

    long countByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    long countByRatingAndCreatedAtBetween(
            Integer rating,
            LocalDateTime start,
            LocalDateTime end
    );

    long countByTopicAndCreatedAtBetween(
            FeedbackTopic topic,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
            SELECT AVG(f.rating)
            FROM Feedback f
            WHERE f.createdAt BETWEEN :start AND :end
            """)
    Double getAverageRatingInPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}