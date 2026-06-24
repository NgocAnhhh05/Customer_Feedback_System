package com.uit.se104.feedback_system.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "user_id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder

public class Customer extends User {

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> listOfSentFeedback = new ArrayList<>();
}