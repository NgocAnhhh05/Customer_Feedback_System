package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.dto.reply.ReplyResponse;
import com.uit.se104.feedback_system.dto.reply.ReplyCreateRequest;
import com.uit.se104.feedback_system.entity.*;
import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import com.uit.se104.feedback_system.exception.*;
import com.uit.se104.feedback_system.repository.ReplyRepository;
import com.uit.se104.feedback_system.repository.FeedbackRepository;

import com.uit.se104.feedback_system.repository.UserRepository;
import com.uit.se104.feedback_system.mapper.EntityMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/*
Service xử lý viết phản hồi giải trình từ Admin.
 */
@Service
@SuppressWarnings("null")
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public ReplyService(ReplyRepository replyRepository, FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.replyRepository = replyRepository;
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    // Admin trả lời phản hồi (UC07) & Tự động cập nhật trạng thái sang RESOLVED (UC08)

    @Transactional
    public ReplyResponse createReply(String feedbackId, ReplyCreateRequest request, String adminId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + feedbackId));

        Admin admin = (Admin) userRepository.findById(adminId)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        Reply reply = Reply.builder()
            .replyId(UUID.randomUUID().toString().replace("-", ""))
            .feedback(feedback)
            .admin(admin)
            .content(request.content().trim())
            .build();

        Reply savedReply = replyRepository.save(reply);

        feedback.setStatus(FeedbackStatus.RESOLVED);
        feedbackRepository.save(feedback);

        return EntityMapper.toReplyResponse(savedReply);
    }

    @Transactional
    public List<ReplyResponse> getRepliesByFeedbackId(String feedbackId){
        List<Reply> replies = replyRepository.findByFeedback_FeedbackIdOrderByCreatedAtDesc(feedbackId);
        return replies.stream().map(EntityMapper::toReplyResponse).toList();
    }

}
