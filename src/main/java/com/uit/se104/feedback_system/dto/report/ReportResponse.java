package com.uit.se104.feedback_system.dto.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponse {
    private String reportUrl;
    private String fileName;
    private String message;
}
