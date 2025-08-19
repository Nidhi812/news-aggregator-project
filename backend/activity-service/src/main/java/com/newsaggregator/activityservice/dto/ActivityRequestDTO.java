package com.newsaggregator.activityservice.dto;

import com.newsaggregator.activityservice.model.ActivityType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityRequestDTO {

    private Long userId;
    private Long newsId;
    private ActivityType activityType;
    private String comment;
}
