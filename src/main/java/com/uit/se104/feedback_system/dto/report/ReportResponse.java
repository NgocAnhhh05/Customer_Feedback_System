package com.uit.se104.feedback_system.dto.report;
import com.uit.se104.feedback_system.entity.enums.ExportType;
import java.time.LocalDateTime;

public record ReportResponse(
    String reportId,
    String title,
    String filterCriteria,
    String dataSummary,
    String managerId,
    String managerName,
    ExportType exportType,
    LocalDateTime createdAt
) {}