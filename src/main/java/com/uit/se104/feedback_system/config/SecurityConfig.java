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
            .csrf(AbstractHttpConfigurer::disable) 
            .authorizeHttpRequests(auth -> auth
                // 🛑 1. OPEN ACCESS FOR VIEW / UI & STATIC RESOURCES (MỞ KHÓA GIAO DIỆN)
                // Cho phép tất cả mọi người truy cập tự do vào các trang HTML giao diện
                .requestMatchers("/", "/dang-ky", "/trang-chu", "/bang-phan-hoi", "/quan-ly-roadmap", "/chi-tiet-feedback", "/sua-feedback", "/them-feedback", "/user-profile").permitAll()
                .requestMatchers("/admin-tong-quan", "/admin-quan-ly-user", "/admin-quan-ly-feedback").permitAll()
                // 🛑 BỔ SUNG THÊM DÒNG NÀY: Chấp nhận mọi phương thức POST/GET gửi lên các đường dẫn UI trên
                .requestMatchers(HttpMethod.POST, "/", "/dang-ky", "/trang-chu", "/bang-phan-hoi").permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/dang-ky", "/trang-chu", "/bang-phan-hoi").permitAll()
                // Cho phép tải các tài nguyên tĩnh như CSS, JS, Ảnh của Frontend
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/favicon.ico").permitAll()
                
                // 🛑 2. OPEN ACCESS FOR H2 DATABASE CONSOLE (MỞ KHÓA DATABASE TRÊN RAM)
                .requestMatchers("/h2-console/**").permitAll()

                // 3. PUBLIC APIS
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()

                // 4. DEVELOPMENT TRICK: Cho phép tạm thời các API hoạt động tự do để bạn dễ test luồng UI
                // Nếu sau này bạn làm xong UI và muốn test phân quyền JWT thật chặt chẽ, chỉ cần COMMENT dòng dưới đây lại!
                .requestMatchers("/api/**").permitAll()

                // 5. SECURE ENDPOINTS (Các luật phân quyền gốc của bạn - Giữ nguyên cấu trúc)
                .requestMatchers(HttpMethod.POST, "/api/feedbacks").hasRole("CUSTOMER")
                .requestMatchers("/api/feedbacks/history").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/api/feedbacks").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/feedbacks/{feedbackId}/replies").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/feedbacks/{feedbackId}/replies").hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/users/admins/{adminId}").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/admins/**").hasRole("MANAGER")
                .requestMatchers("/api/reports/**").hasRole("MANAGER")

                // 6. CÒN LẠI
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 🛑 7. BẮT BUỘC PHẢI THÊM: Tắt chặn frame để hiển thị được giao diện bảng H2 Console
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // Đồng bộ passwordEncoder với hệ thống mã hóa mật khẩu
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
