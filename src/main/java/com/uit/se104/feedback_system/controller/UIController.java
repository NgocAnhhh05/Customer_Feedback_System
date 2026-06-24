package com.uit.se104.feedback_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    // Đường ray số 1: Mở trang chủ đầu tiên
    @GetMapping("/")
    public String trangChu() { return "trang-chu"; }

    // Đường ray số 2: Trang đăng ký
    @GetMapping("/dang-ky")
    public String dangKy() { return "dang-ky"; }

    // Đường ray số 3: Bảng phản hồi (User)
    @GetMapping("/bang-phan-hoi")
    public String bangPhanHoi() { return "bang-phan-hoi"; }

    // Đường ray số 4: Chi tiết phản hồi
    @GetMapping("/chi-tiet-feedback")
    public String chiTiet() { return "chi-tiet-feedback"; }

    // Đường ray số 5: Thêm phản hồi (Cái này quan trọng nhất)
    @GetMapping("/them-feedback")
    public String themFeedback() { return "them-feedback"; }

    // Đường ray số 6: Sửa phản hồi
    @GetMapping("/sua-feedback")
    public String suaFeedback() { return "sua-feedback"; }

    // Đường ray số 7: Lộ trình sản phẩm
    @GetMapping("/quan-ly-roadmap")
    public String roadmap() { return "quan-ly-roadmap"; }

    // --- CÁC TRANG CỦA ADMIN ---
    @GetMapping("/admin-tong-quan")
    public String adminTongQuan() { return "admin-tong-quan"; }

    @GetMapping("/admin-quan-ly-feedback")
    public String adminFeedback() { return "admin-quan-ly-feedback"; }

    @GetMapping("/admin-quan-ly-user")
    public String adminUser() { return "admin-quan-ly-user"; }
}