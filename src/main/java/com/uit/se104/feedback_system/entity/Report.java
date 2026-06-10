package com.uit.se104.feedback_system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.uit.se104.feedback_system.entity.enums.ExportType;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Report extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(name = "filter_criteria")
    private String filterCriteria; // Tiêu chí lọc (Dưới dạng JSON string)

    @Column(name = "data_summary", columnDefinition = "TEXT")
    private String dataSummary; // Tóm tắt dữ liệu thống kê

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @Enumerated(EnumType.STRING)
    @Column(name = "export_type", nullable = false)
    private ExportType exportType;

}
