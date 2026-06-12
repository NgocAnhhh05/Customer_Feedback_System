package com.uit.se104.feedback_system.mapper;

import com.uit.se104.feedback_system.dto.feedback.FeedbackResponse;
import com.uit.se104.feedback_system.dto.feedback.AttachmentResponse;
import com.uit.se104.feedback_system.dto.reply.ReplyResponse;
import com.uit.se104.feedback_system.dto.report.ReportResponse;
import com.uit.se104.feedback_system.dto.user.AdminResponse;
import com.uit.se104.feedback_system.dto.user.ManagerResponse;
import com.uit.se104.feedback_system.dto.user.UserResponse;
import com.uit.se104.feedback_system.entity.*;
import com.uit.se104.feedback_system.repository.ManagerRepository;

import java.util.Collections;
import java.util.List;

public final class EntityMapper {
    private EntityMapper(){
        // ban self-initialize
    }

    public static UserResponse toUserResponse(User user){
        if (user == null) return null;

        return new UserResponse(
            user.getUserId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }


    public static AttachmentResponse toAttachmentResponse(Attachment attachment){
        if (attachment == null) return null;

        return new AttachmentResponse(
            attachment.getAttachmentId(),
            attachment.getFilePath(),
            attachment.getFileType(),
            attachment.getFileSize()
        );
    }

    public static ReplyResponse toReplyResponse(Reply reply){
        if (reply == null) return null;

        return new ReplyResponse(
            reply.getReplyId(),
            reply.getFeedback().getFeedbackId(),
            reply.getAdmin().getUserId(),
            reply.getAdmin().getName(),
            reply.getContent(),
            reply.getCreatedAt(),
            reply.getUpdateAt()
        );
    }

    public static FeedbackResponse toFeedbackResponse(Feedback feedback){
        if (feedback == null) return null;

        List<AttachmentResponse> attachmentResponses = feedback.getAttachments() != null ? feedback.getAttachments().stream().map(EntityMapper::toAttachmentResponse).toList() : Collections.emptyList();

        List<ReplyResponse> replyResponses = feedback.getReplies() != null ? feedback.getReplies().stream().map(EntityMapper::toReplyResponse).toList() : Collections.emptyList();

        return new FeedbackResponse(
            feedback.getFeedbackId(),
            feedback.getCustomer().getUserId(),
            feedback.getCustomer().getName(),
            feedback.getContent(),
            feedback.getTopic(),
            feedback.getRating(),
            feedback.getStatus(),
            feedback.getCreatedAt(),
            feedback.getUpdateAt(),
            attachmentResponses,
            replyResponses
        );
    }

    public static ReportResponse toReportResponse(Report report){
        if (report == null) return null;

        return new ReportResponse(
            report.getReportId(),
            report.getTitle(),
            report.getFilterCriteria(),
            report.getDataSummary(),
            report.getManager().getUserId(),
            report.getManager().getName(),
            report.getExportType(),
            report.getCreatedAt()
        );
    }

    public static AdminResponse toAdminResponse(Admin admin){
        if (admin == null) return null;

        return new AdminResponse(
            admin.getUserId(),
            admin.getEmail(),
            admin.getName(),
            admin.getSpecialization(),
            admin.isWorkingStatus()
        );
    }

    public static ManagerResponse toManagerResponse(Manager manager){
        if (manager == null) return null;

        return new ManagerResponse(
            manager.getUserId(),
            manager.getName(),
            manager.getEmail(),
            manager.getManagementLevel(),
            manager.getManagerDepartment()
        );
    }

}
