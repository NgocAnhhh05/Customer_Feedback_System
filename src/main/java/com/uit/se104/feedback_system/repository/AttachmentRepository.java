package com.uit.se104.feedback_system.repository;

import com.uit.se104.feedback_system.entity.Attachment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    List<Attachment> findAllByFeedbackFeedbackId(String feedbackId);

}