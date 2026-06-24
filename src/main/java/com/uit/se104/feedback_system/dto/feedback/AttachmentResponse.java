package com.uit.se104.feedback_system.dto.feedback;
import com.uit.se104.feedback_system.entity.enums.AttachmentType;

public record AttachmentResponse(
    String attachmentId,
    String filePath,
    AttachmentType fileType,
    Long fileSize
) {}