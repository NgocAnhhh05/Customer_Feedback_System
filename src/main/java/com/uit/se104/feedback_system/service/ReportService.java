package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.entity.Report;
import com.uit.se104.feedback_system.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public Report generateReport(Report report) {
        return reportRepository.save(report);
    }
}
