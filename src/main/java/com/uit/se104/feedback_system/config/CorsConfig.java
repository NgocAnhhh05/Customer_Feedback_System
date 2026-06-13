package com.uit.se104.feedback_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/**
 * Cấu hình CORS (Cross-Origin Resource Sharing)
 * Giúp Frontend (chạy trên cổng khác như 3000, 5173) có thể gọi API Backend mà không bị trình duyệt chặn.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Cho phép gửi kèm cookie và thông tin xác thực (Credentials)
        config.setAllowCredentials(true);

        // Cấu hình các địa chỉ Frontend được phép truy cập (Ví dụ: React, Vue, HTML Live Server)
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        // Cho phép tất cả các Header HTTP gửi lên
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-User-Id"));

        // Cho phép tất cả các phương thức HTTP (GET, POST, PUT, DELETE, OPTIONS)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}