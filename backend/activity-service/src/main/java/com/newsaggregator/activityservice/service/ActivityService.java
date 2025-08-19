package com.newsaggregator.activityservice.service;

import java.util.List;

import com.newsaggregator.activityservice.dto.ActivityRequestDTO;
import com.newsaggregator.activityservice.dto.ActivityResponseDTO;

public interface ActivityService {

    ActivityResponseDTO logActivity(ActivityRequestDTO requestDTO);

    List<ActivityResponseDTO> getActivitiesByUser(Long userId);

    List<ActivityResponseDTO> getActivitiesByNews(Long newsId);

    // ✅ Unlike support: deletes a LIKE activity by user and news
    void removeLike(Long userId, Long newsId);
}
