package com.uit.se104.feedback_system.dto.report;
import java.util.Map;

public record AnalyticsResponse(
    long totalFeedbacks,
    Double averageRating,
    Map<Integer, Long> ratingDistribution,
    Map<String, Long> feedbackByTopic
) {
}

// example
// {
//   "totalFeedback": 500,
//   "averageRating": 4.3,

//   "ratingDistribution": {
//     "1": 10,
//     "2": 20,
//     "3": 50,
//     "4": 120,
//     "5": 300
//   },

//   "feedbackByTopic": {
//     "PRODUCT_ISSUE": 100,
//     "SERVICE_QUALITY": 250,
//     "DELIVERY_ISSUE": 150
//   }
// }