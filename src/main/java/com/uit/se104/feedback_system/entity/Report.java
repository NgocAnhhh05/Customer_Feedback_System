package com.uit.se104.feedback_system.entity;

import com.uit.se104.feedback_system.entity.enums.ExportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
public class Report extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "export_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExportType exportType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "generated_by")
    private String generatedBy;
}
