package com.uit.se104.feedback_system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reply")

public class Reply extends BaseEntity{

    @Id
    @Column(name = "reply_id", nullable = false)
    private String replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;
    // many replies belong just to 1 feedback

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String context;

}
