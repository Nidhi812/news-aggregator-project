package com.newsaggregator.activityservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "activity",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "newsId", "activityType"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long newsId;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    private String comment;

    private LocalDateTime timestamp;
}
