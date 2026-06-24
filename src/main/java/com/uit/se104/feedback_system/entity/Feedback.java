package com.uit.se104.feedback_system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;
import java.util.ArrayList;

import com.uit.se104.feedback_system.entity.enums.FeedbackStatus;
import com.uit.se104.feedback_system.entity.enums.FeedbackTopic;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feedback")
public class Feedback extends BaseEntity {

    @Id
    @Column(name = "feedback_id")
    private String feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable=false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedbackTopic topic;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    @Column(nullable = false)
    private Integer rating;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reply> replies = new ArrayList<>();

}
