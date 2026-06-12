package com.uit.se104.feedback_system.dto.common;

public record ApiResponse<T>(
    boolean success,
    String message,
    T data

) {}

// ex
// return ResponseEntity.ok(
//     new ApiResponse<>(
//         true,
//         "Feedback created successfully",
//         feedbackResponse
//     )
// );

// --> frontend receive
// {
//   "success": true,
//   "message": "Feedback created successfully",
//   "data": {
//       ...
//   }
// }