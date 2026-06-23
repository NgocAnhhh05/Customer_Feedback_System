package com.uit.se104.feedback_system.mapper;

import com.uit.se104.feedback_system.dto.feedback.FeedbackResponse;
import com.uit.se104.feedback_system.dto.feedback.AttachmentResponse;
import com.uit.se104.feedback_system.dto.reply.ReplyResponse;
import com.uit.se104.feedback_system.dto.report.ReportResponse;
import com.uit.se104.feedback_system.dto.user.AdminResponse;
import com.uit.se104.feedback_system.dto.user.ManagerResponse;
import com.uit.se104.feedback_system.dto.user.UserResponse;
import com.uit.se104.feedback_system.entity.*;

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

        // 🛑 BẢO VỆ CHỐNG NULL CAO CẤP: Kiểm tra thực thể Admin liên kết trước khi đọc thuộc tính
        String adminId = (reply.getAdmin() != null) ? reply.getAdmin().getUserId() : "UNKNOWN";
        String adminName = (reply.getAdmin() != null) ? reply.getAdmin().getName() : "Hệ thống / Vô danh";
        String feedbackId = (reply.getFeedback() != null) ? reply.getFeedback().getFeedbackId() : null;

        return new ReplyResponse(
            reply.getReplyId(),
            feedbackId,
            adminId,
            adminName,
            reply.getContent(),
            reply.getCreatedAt(),
            reply.getUpdateAt()
        );
    }

    public static FeedbackResponse toFeedbackResponse(Feedback feedback){
        if (feedback == null) return null;

        List<AttachmentResponse> attachmentResponses = feedback.getAttachments() != null 
            ? feedback.getAttachments().stream().map(EntityMapper::toAttachmentResponse).toList() 
            : Collections.emptyList();

        List<ReplyResponse> replyResponses = feedback.getReplies() != null 
            ? feedback.getReplies().stream().map(EntityMapper::toReplyResponse).toList() 
            : Collections.emptyList();

        // 🛑 BẢO VỆ CHỐNG NULL CAO CẤP: Phòng ngừa kịch bản Customer bị null làm ném lỗi 500
        String customerId = (feedback.getCustomer() != null) ? feedback.getCustomer().getUserId() : "UNKNOWN";
        String customerName = (feedback.getCustomer() != null) ? feedback.getCustomer().getName() : "Khách vãng lai";

        return new FeedbackResponse(
            feedback.getFeedbackId(),
            customerId,
            customerName,
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

        // 🛑 BẢO VỆ CHỐNG NULL: Tránh lỗi sập khi Manager liên kết bị trống
        String managerId = (report.getManager() != null) ? report.getManager().getUserId() : "UNKNOWN";
        String managerName = (report.getManager() != null) ? report.getManager().getName() : "Quản lý hệ thống";

        return new ReportResponse(
            report.getReportId(),
            report.getTitle(),
            report.getFilterCriteria(),
            report.getDataSummary(),
            managerId,
            managerName,
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
            manager.getManageDepartment()
        );
    }
}
