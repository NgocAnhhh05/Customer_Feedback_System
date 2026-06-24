package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.dto.report.*;
import com.uit.se104.feedback_system.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

     /*
     - API Manager truy cập xem phân tích số liệu thống kê Dashboard dạng biểu đồ (UC09)
     - GET http://localhost:8080/api/reports/analytics?startDate=2026-01-01T00:00:00&endDate=2026-12-31T23:59:59
     */
    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        AnalyticsResponse response = reportService.getAnalyticsData(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    /*
     - API Manager yêu cầu xuất file báo cáo định dạng Excel hoặc PDF (UC09)
     - POST http://localhost:8080/api/reports/export
     */
    @PostMapping("/export")
    public ResponseEntity<ReportResponse> exportReport(
        @Valid @RequestBody ReportRequest request,
        @RequestHeader("X-Manager-Id") String managerId
    ){
        ReportResponse response = reportService.generateAndSaveReport(request, managerId);
        return ResponseEntity.ok(response);
    }


}
