package com.newsaggregator.activityservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsaggregator.activityservice.model.Activity;
import com.newsaggregator.activityservice.model.ActivityType;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserId(Long userId);
    List<Activity> findByNewsId(Long newsId);

    // ✅ Updated to return List instead of Optional
    List<Activity> findByUserIdAndNewsIdAndActivityType(Long userId, Long newsId, ActivityType activityType);
}
