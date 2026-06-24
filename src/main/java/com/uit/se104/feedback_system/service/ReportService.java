package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.dto.report.*;
import com.uit.se104.feedback_system.entity.*;
import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;

import com.uit.se104.feedback_system.exception.*;
import com.uit.se104.feedback_system.repository.ReportRepository;
import com.uit.se104.feedback_system.repository.FeedbackRepository;

import com.uit.se104.feedback_system.repository.ManagerRepository;

import com.uit.se104.feedback_system.mapper.EntityMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;


/*
Service tính toán báo cáo thống kê cho quản trị viên.
*/
@Service
@SuppressWarnings("null")
public class ReportService {

    private final FeedbackRepository feedbackRepository;
    private final ReportRepository reportRepository;
    private final ManagerRepository managerRepository;

    public ReportService(FeedbackRepository feedbackRepository, ReportRepository reportRepository, ManagerRepository managerRepository) {
        this.feedbackRepository = feedbackRepository;
        this.reportRepository = reportRepository;
        this.managerRepository = managerRepository;
    }

    // Lấy dữ liệu phân tích trực quan Dashboard (UC09)
    @Transactional(readOnly = true)
    public AnalyticsResponse getAnalyticsData(LocalDateTime start, LocalDateTime end) {
        long totalFeedbacks = feedbackRepository.countByCreatedAtBetween(start, end);
        Double avgRating = feedbackRepository.getAverageRatingInPeriod(start, end);
        if (avgRating == null) avgRating = 0.0;

        Map<Integer, Long> ratingDistribution = new HashMap<>();
        for (int star = 1; star <= 5; star++) {
            long count = feedbackRepository.countByRatingAndCreatedAtBetween(star, start, end);
            ratingDistribution.put(star, count);
        }

        Map<String, Long> feedbacksByTopic = new HashMap<>();
        for (FeedbackTopic topic : FeedbackTopic.values()) {
            long count = feedbackRepository.countByTopicAndCreatedAtBetween(topic, start, end);
            feedbacksByTopic.put(topic.name(), count);
        }

        return new AnalyticsResponse(totalFeedbacks, avgRating, ratingDistribution, feedbacksByTopic);
    }

    // Xuất báo cáo và lưu nhật ký (UC09)
    @Transactional
    public ReportResponse generateAndSaveReport(ReportRequest request, String managerId) {
        Manager manager = managerRepository.findById(managerId)
            .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));

        AnalyticsResponse data = getAnalyticsData(request.startDate(), request.endDate());
        String summaryText = String.format("Total Feedbacks: %d | Avg Rating: %.2f stars",
            data.totalFeedbacks(), data.averageRating());

        Report report = Report.builder()
            .reportId(UUID.randomUUID().toString().replace("-", ""))
            .title(request.title().trim())
            .filterCriteria(String.format("Start: %s, End: %s", request.startDate(), request.endDate()))
            .dataSummary(summaryText)
            .manager(manager)
            .exportType(request.exportType())
            .build();

        Report savedReport = reportRepository.save(report);
        return EntityMapper.toReportResponse(savedReport);
    }
}
