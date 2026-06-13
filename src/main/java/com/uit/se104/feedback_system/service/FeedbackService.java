package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.dto.feedback.*;
import com.uit.se104.feedback_system.dto.user.UserResponse;
import com.uit.se104.feedback_system.entity.*;
import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;
import com.uit.se104.feedback_system.exception.*;
import com.uit.se104.feedback_system.repository.FeedbackRepository;
import com.uit.se104.feedback_system.repository.UserRepository;
import com.uit.se104.feedback_system.repository.CustomerRepository;
import com.uit.se104.feedback_system.repository.AttachmentRepository;
import com.uit.se104.feedback_system.entity.enums.AttachmentType;

import com.uit.se104.feedback_system.mapper.EntityMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;


    public FeedbackService(FeedbackRepository feedbackRepository,
                     UserRepository userRepository,
                     CustomerRepository customerRepository,
                     AttachmentRepository attachmentRepository,
                     FileStorageService fileStorageService){
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    // customer send a feedback
    @Transactional
    public FeedbackResponse createFeedback(FeedbackCreateRequest request, String customerId, MultipartFile[] files){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with Id: " + customerId));

        // 1. create and save feedback first
        Feedback feedback = Feedback.builder()
            .feedbackId(UUID.randomUUID().toString().replace("-", ""))
            .customer(customer)
            .content(request.content().trim())
            .topic(request.topic())
            .rating(request.rating())
            .status(FeedbackStatus.PENDING)
            .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        // 2. handle attachments if any
        if (files != null && files.length > 0){
            for (MultipartFile file : files){
                if (!file.isEmpty()){
                    // save relativePath
                    String relativePath = fileStorageService.storeFile(file);

                    // save file type
                    AttachmentType type = file.getContentType().equals("application/pdf") ? AttachmentType.PDF : AttachmentType.IMAGE;

                    Attachment attachment = Attachment.builder()
                        .attachmentId(UUID.randomUUID().toString().replace("-", ""))
                        .feedback(savedFeedback)
                        .filePath(relativePath)
                        .fileSize(file.getSize())
                        .fileType(type)
                        .build();
                    attachmentRepository.save(attachment);
                }
            }
        }
        return EntityMapper.toFeedbackResponse(savedFeedback);
    }


    // Customer can see their list of sent feedback
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getCustomerFeedbackHistory(String customerId, FeedbackStatus status){
        if(!userRepository.existsById(customerId)){
            throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
        }
        List<Feedback> feedbacks = (status != null) ? feedbackRepository.findAllByCustomer_UserIdAndStatusOrderByCreatedAtDesc(customerId, status) : feedbackRepository.findAllByCustomer_UserIdOrderByCreatedAtDesc(customerId);

        return feedbacks.stream().map(EntityMapper::toFeedbackResponse).toList();
    }

    // Admin duyệt danh sách phản hồi (UC05)
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbacks(String keyword) {
        List<Feedback> feedbacks = (keyword != null && !keyword.trim().isEmpty())
            ? feedbackRepository.findByContentContainingIgnoreCase(keyword.trim())
            : feedbackRepository.findAllByOrderByCreatedAtDesc();

        return feedbacks.stream().map(EntityMapper::toFeedbackResponse).toList();
    }

    // Admin phân loại phản hồi (UC06)

    @Transactional
    public FeedbackResponse classifyFeedback(String feedbackId, FeedbackTopic topic) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + feedbackId));

        feedback.setTopic(topic);
        feedback.setStatus(FeedbackStatus.IN_PROGRESS); // Chuyển trạng thái sang Đang xử lý

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return EntityMapper.toFeedbackResponse(updatedFeedback);
    }

    //Admin cập nhật trạng thái đánh giá (UC08)
    @Transactional
    public FeedbackResponse updateStatus(String feedbackId, FeedbackStatus status) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + feedbackId));

        feedback.setStatus(status);

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return EntityMapper.toFeedbackResponse(updatedFeedback);
    }

}
