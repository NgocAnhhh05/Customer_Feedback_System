# 🚀 HƯỚNG DẪN CHẠY BACKEND LOCAL - CUSTOMER FEEDBACK SYSTEM

Tài liệu này hướng dẫn chi tiết cho các thành viên trong nhóm (**Linh** - Frontend và **Hoàng** - Tester) cách cấu hình, khởi chạy và kết nối hệ thống Backend trên máy cá nhân để phục vụ việc ghép giao diện và làm kịch bản kiểm thử.

---

## 📌 1. Yêu Cầu Cấu Hình Môi Trường (Prerequisites)

Trước khi bắt đầu, hãy đảm bảo máy tính của các bạn đã cài đặt các công cụ sau:
* **Java Development Kit (JDK):** Phiên bản **Java 17**
* **Database:** **PostgreSQL** (Khuyên dùng phiên bản 15 trở lên).
* **Công cụ quản lý DB (Tùy chọn):** dBeaver, pgAdmin, hoặc DataGrip.
* **Công cụ Test API:** **Postman** (Để import file Collection của nhóm).

---

## 🛠️ 2. Các Bước Khởi Tạo Database (Làm 1 lần đầu tiên)

Dự án này sử dụng tính năng tự động tạo cấu trúc bảng hệ thống của Hibernate (`ddl-auto=update`), do đó các bạn **không cần chạy bất kỳ file SQL cấu trúc nào cả**. Hãy làm theo 2 bước sau:

1. Mở pgAdmin hoặc dBeaver lên, kết nối vào PostgreSQL server trên máy của bạn.
2. Tạo một Database trống tinh với tên chính xác là: **`feedback_system`**.

---

## 📝 3. Cấu Hình File `application.properties`

Carol đã chuẩn hóa tài khoản kết nối Database cho cả nhóm để không ai phải sửa code của nhau mỗi khi pull về. Hãy vào file `src/main/resources/application.properties` kiểm tra xem thông số đã khớp chưa:

```properties
spring.application.name=feedback-system

# URL kết nối đến Database trống vừa tạo ở Bước 2
spring.datasource.url=jdbc:postgresql://localhost:5432/feedback_system

# Tài khoản PostgreSQL - điền các nội dụng các bạn vừa setup database vào bên dưới
# Lưu ý: KHÔNG COMMIT FILE PROPERTIES NÀY LÊN GITHUB
spring.datasource.username=postgres
spring.datasource.password=123456

# Cơ chế tự động đồng bộ thực thể Java thành bảng trong DB
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

> ⚠️ **Lưu ý quan trọng cho Linh và Hoàng:** > Nếu lúc cài PostgreSQL trên máy các bạn, các bạn lỡ đặt mật khẩu Admin khác `123456` (ví dụ: `chucmungnammoi`, `root`, `123`), các bạn có thể sửa tạm dòng `spring.datasource.password` thành mật khẩu máy mình để chạy. **Tuy nhiên, khi commit/push lên GitHub, tuyệt đối không commit file thay đổi mật khẩu này lên để tránh bị xung đột code với cả nhóm **

---

## 🏃‍♂️ 4. Cách Khởi Chạy Ứng Dụng (Run Backend)

Các bạn có thể chạy ứng dụng bằng 1 trong 2 cách sau:

### Cách A: Chạy bằng Terminal (Khuyên dùng để check lỗi nhanh)

Mở Terminal tại thư mục gốc của dự án (`/feedback_system`) và gõ lệnh:

```bash
# Đối với Mac / Linux:
./mvnw spring-boot:run

# Đối với Windows (Command Prompt / PowerShell):
mvnw.cmd spring-boot:run

```

### Cách B: Chạy trực tiếp trên IDE (IntelliJ / VS Code)

1. Mở dự án bằng IDE của bạn.
2. Tìm đến file theo đường dẫn: `src/main/java/com/uit/se104/feedback_system/FeedbackSystemApplication.java`.
3. Bấm vào nút **Run** (Hình tam giác màu xanh) hoặc nút **Debug** để chạy hệ thống.

> 🌟 **Dấu hiệu chạy thành công:** Khi dòng cuối cùng của tab Console hiển thị:
> `Started FeedbackSystemApplication in X seconds (process started by...)` và port mặc định là `8080`. Lúc này hệ thống đã sẵn sàng nhận request!

---

## 🔑 5. Tài Khoản Đăng Nhập Mặc Định (Seed Data)

Hệ thống đã được tích hợp cơ chế tự động kiểm tra database khi khởi chạy. Nếu database trống, hệ thống sẽ tự động tạo sẵn một tài khoản **Manager cấp cao nhất** phục vụ cho lần đăng nhập đầu tiên để lấy Token quản trị.

Các bạn sử dụng thông tin sau để điền vào request Login trên Postman/Frontend:

* **Username:** `root_manager`
* **Password:** `ManagerPassword123!`
* **Quyền hạn (Role):** `ROLE_MANAGER`

---

## 📬 6. Hướng Dẫn Bàn Giao Đầu Việc Kế Tiếp

### 👩‍💻 Đối với Linh (Frontend - Thymeleaf)

* Ngọc Anh đã cấu hình phân quyền và CORS đầy đủ. Hãy pull code Backend mới nhất này về, chạy lên ở local máy.
* Các file giao diện HTML đặt trong thư mục `src/main/resources/templates`.
* Khi gửi dữ liệu (Form Submit hoặc gọi AJAX/Fetch) lên các API yêu cầu định danh (như Gửi Feedback), nhớ đính kèm Custom Header tên là `X-Customer-Id` vào HTTP Request như tớ đã note trong file Postman.

### 👨‍🔬 Đối với Hoàng (Tester)

* Import file `SE104 - Customer Feedback System.postman_collection.json` tớ gửi trong nhóm chat vào Postman để lấy bộ request mẫu.
* Viết integration test trong folder `test/` và cả test bằng Postman
* Hãy chạy Backend lên và dùng Postman chạy thử theo luồng tự nhiên: *Đăng nhập Manager $\rightarrow$ Tạo Admin $\rightarrow$ Đăng ký Customer $\rightarrow$ Gửi Feedback kèm file ảnh/pdf thực tế từ máy .*
* Kiểm tra xem các hàm xử lý Exception có trả về đúng mã lỗi `400 BadRequest` hay `401 Unauthorized` khi cố tình truyền thiếu dữ liệu hoặc truyền sai quyền không.

---

Chúc team mình phối hợp mượt mà và hoàn thành xuất sắc đồ án môn SE104! Có bất kỳ lỗi crash hay lỗi kết nối nào, hãy chụp màn hình log Console gửi lên nhóm để hỗ trợ ngay.

```

```