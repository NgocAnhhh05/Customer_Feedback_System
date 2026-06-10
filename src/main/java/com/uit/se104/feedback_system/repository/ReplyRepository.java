package com.uit.se104.feedback_system.repository;

import com.uit.se104.feedback_system.entity.Reply;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {

    List<Reply> findByFeedback_FeedbackIdOrderByCreatedAtDesc(String feedbackId);
}