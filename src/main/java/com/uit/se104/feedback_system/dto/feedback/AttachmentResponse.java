package com.uit.se104.feedback_system.dto.feedback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
}
