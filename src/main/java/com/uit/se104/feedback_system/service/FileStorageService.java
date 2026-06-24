package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    // Thư mục gốc để lưu file trong project
    private final Path rootLocation = Paths.get("uploads");

    public FileStorageService() {
        try {
            // Tự động tạo thư mục uploads nếu chưa có
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file.");
        }

        // 1. Kiểm tra định dạng file (Chỉ cho phép JPG, PNG, PDF)
        String contentType = file.getContentType();
        if (contentType == null ||
            (!contentType.equals("image/jpeg") &&
             !contentType.equals("image/png") &&
             !contentType.equals("application/pdf"))) {
            throw new BadRequestException("Invalid file type! Only JPG, PNG, and PDF are allowed.");
        }

        try {
            // 2. Chuẩn hóa tên file để tránh bị trùng lặp bằng cách thêm UUID
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFilename = UUID.randomUUID().toString() + extension;

            // 3. Thực hiện copy luồng bytes của file vào thư mục uploads
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedFilename))
                    .normalize().toAbsolutePath();

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // Trả về tên file hoặc đường dẫn tương đối để lưu vào DB
            return "/uploads/" + storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}