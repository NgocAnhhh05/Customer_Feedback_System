package com.uit.se104.feedback_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
- Lỗi ném ra khi dữ liệu gửi lên không hợp lệ hoặc vi phạm ràng buộc nghiệp vụ (ví dụ: Trùng email, trùng tên tài khoản)
- Trả về mã lỗi HTTP 400 BAD REQUEST cho Frontend.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
