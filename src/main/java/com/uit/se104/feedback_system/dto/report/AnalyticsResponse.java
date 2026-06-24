package com.uit.se104.feedback_system.dto.report;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class AnalyticsResponse {
    private long totalFeedbackCount;
    private long pendingCount;
    private long resolvedCount;
    private double averageRating;
    private Map<Integer, Long> ratingDistribution;
    private Map<String, Long> topicDistribution;
}
