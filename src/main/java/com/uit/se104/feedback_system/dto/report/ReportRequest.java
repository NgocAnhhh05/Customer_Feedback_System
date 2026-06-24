package com.uit.se104.feedback_system.dto.report;

import com.uit.se104.feedback_system.entity.enums.ExportType;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record ReportRequest(
    @NotBlank(message = "Report title is required")
    String title,

    @NotNull(message = "Start date filter is required")
    LocalDateTime startDate,

    @NotNull(message = "End date filter is required")
    LocalDateTime endDate,

    @NotNull(message = "Export format (Excel/PDF) is required")
    ExportType exportType

) {}