# Customer Feedback System (Hệ thống quản lý phản hồi khách hàng)

### Môn học: SE104 - Nhập môn Công nghệ Phần mềm | UIT

Dự án này là hệ thống thu thập, phân loại và quản lý phản hồi của khách hàng, được xây dựng theo mô hình **Full-stack (Spring Boot + React SPA)**.

---

## 🛠️ Yêu cầu hệ thống

Trước khi bắt đầu, đảm bảo máy tính của bạn đã cài đặt các công cụ sau:
* **Java Development Kit (JDK):** Phiên bản 17 trở lên.
* **Node.js:** Phiên bản 18 trở lên (Khuyến nghị bản LTS).
* **Apache Maven:** Đã được tích hợp sẵn qua trình bao wrapper `./mvnw`.
* **Cơ sở dữ liệu:** PostgreSQL (Cấu hình mặc định chạy trên cổng `5432`).

---

## 📂 Cấu trúc thư mục chính

```text
customer-feedback-system/
├── src/
│   ├── main/
│   │   ├── java/com/uit/se104/feedback_system/
│   │   │   ├── controller/          # Định nghĩa các API Endpoints & UI Controller
│   │   │   ├── entity/              # Lớp bản đồ cơ sở dữ liệu (User, Feedback...)
│   │   │   ├── dto/                 # Khối dữ liệu trao đổi (ApiResponse, Requests...)
│   │   │   ├── mapper/              # Chuyển đổi giữa Entity và DTO (Lombok)
│   │   │   ├── security/            # Cấu hình bảo mật JWT & Spring Security
│   │   │   └── service/             # Xử lý logic nghiệp vụ chính của hệ thống
│   │   └── resources/
│   │       ├── application.yml      # Cấu hình cổng, kết nối cơ sở dữ liệu PostgreSQL
│   │       └── templates/           # Mã nguồn giao diện cũ (Thymeleaf .html backup)
│   └── App.tsx                      # Giao diện chính của ứng dụng Single Page Application
│   └── main.tsx                     # Điểm khởi chạy của ứng dụng React/Vite
├── pom.xml                          # Quản lý thư viện Backend Java (Spring Boot)
├── package.json                     # Quản lý thư viện Frontend JavaScript (React)
└── vite.config.ts                   # Cấu hình công cụ đóng gói và Dev Server Vite
```
---

## Hướng dẫn khởi chạy ứng dụng

Cài đặt thư viện: Mở cửa sổ Terminal tại thư mục gốc của dự án và chạy lệnh:

`npm install`

Khởi chạy môi trường phát triển (Dev Server):

`npm run dev`
Trải nghiệm ứng dụng: Mở trình duyệt và truy cập đường dẫn được hiển thị trên Terminal:

Địa chỉ Local: `http://localhost:3000`

Địa chỉ Mạng nội bộ (Network): `http://192.168.1.5:3000` (Dùng để test giao diện trực tiếp bằng điện thoại kết nối chung Wi-Fi).