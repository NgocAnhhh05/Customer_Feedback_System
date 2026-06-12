package com.uit.se104.feedback_system.dto.common;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String message,
    Map<String, String> errors // list of detailed error
) {
    public ApiErrorResponse(LocalDateTime timestamp, Integer status, String error, String message){
        this(timestamp, status, error, message, null); // null vì không phải lỗi nào cũng có field bị sai
    }
}