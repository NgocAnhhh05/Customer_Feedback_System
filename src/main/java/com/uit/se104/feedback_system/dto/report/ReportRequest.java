package com.uit.se104.feedback_system.dto.report;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ReportRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private String exportType; // EXCEL, PDF, CSV
}
