package com.uit.se104.feedback_system.config;

import com.uit.se104.feedback_system.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 - Cấu hình bảo mật chính cho ứng dụng Spring Security.
 - Phân quyền truy cập tài nguyên cho các vai trò khác nhau (UC01 -> UC10).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF vì dùng Stateless JWT
            .authorizeHttpRequests(auth -> auth
                // 1. PUBLIC APIS
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()

                // 2. CUSTOMER ENDPOINTS
                // Chỉ Khách hàng mới được gửi feedback và xem lịch sử của chính họ
                .requestMatchers(HttpMethod.POST, "/api/feedbacks").hasRole("CUSTOMER")
                .requestMatchers("/api/feedbacks/history").hasRole("CUSTOMER")

                // 3. ADMIN / MANAGER ENDPOINTS
                // Admin và Manager được duyệt xem danh sách feedback tổng quát (UC05)
                .requestMatchers(HttpMethod.GET, "/api/feedbacks").hasAnyRole("ADMIN", "MANAGER")

                // Admin và Manager được phép viết câu trả lời phản hồi (UC07)
                .requestMatchers(HttpMethod.POST, "/api/feedbacks/{feedbackId}/replies").hasAnyRole("ADMIN", "MANAGER")

                // 4. SHARED ENDPOINTS
                // Cả Khách hàng (để đọc) và Admin (để xem lịch sử) đều được phép xem danh sách replies
                .requestMatchers(HttpMethod.GET, "/api/feedbacks/{feedbackId}/replies").hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")

                // 5. MANAGER ONLY ENDPOINTS
                // Quản lý nhân viên (UserController cũ)
                .requestMatchers(HttpMethod.PUT, "/api/users/admins/{adminId}").hasAnyRole("ADMIN", "MANAGER") // Admin tự sửa thông tin
                .requestMatchers("/api/users/admins/**").hasRole("MANAGER") // Các tác vụ CRUD admin còn lại

                // Xem dashboard và Xuất báo cáo (ReportController - UC09)
                .requestMatchers("/api/reports/**").hasRole("MANAGER")

                // 6. CÒN LẠI
                // Tất cả các request khác chưa được định nghĩa ở trên thì chỉ cần đăng nhập là được
                .anyRequest().authenticated()
            )
            // Cấu hình quản lý Session là STATELESS (Không lưu phiên làm việc trên server, dùng JWT để xác thực từng request)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Thêm bộ lọc kiểm tra JWT trước khi Spring Security xác thực thông tin đăng nhập mặc định
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}