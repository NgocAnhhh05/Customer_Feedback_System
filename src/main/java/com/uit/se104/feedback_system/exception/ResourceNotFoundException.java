package com.uit.se104.feedback_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 - Lỗi ném ra khi không tìm thấy tài nguyên trong Cơ sở dữ liệu (vd: Không tìm thấy User ID, Feedback ID)
 - Trả về mã lỗi HTTP 404 NOT FOUND cho Frontend.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
