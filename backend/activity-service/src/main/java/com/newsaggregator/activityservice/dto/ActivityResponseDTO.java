package com.newsaggregator.activityservice.dto;

import java.time.LocalDateTime;

import com.newsaggregator.activityservice.model.ActivityType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponseDTO {

    private Long id;
    private Long userId;
    private Long newsId;
    private ActivityType activityType;
    private String comment;
    private LocalDateTime timestamp;
}
