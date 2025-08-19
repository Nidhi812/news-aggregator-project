package com.newsaggregator.activityservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.newsaggregator.activityservice.dto.ActivityRequestDTO;
import com.newsaggregator.activityservice.dto.ActivityResponseDTO;
import com.newsaggregator.activityservice.model.Activity;
import com.newsaggregator.activityservice.model.ActivityType;
import com.newsaggregator.activityservice.repository.ActivityRepository;
import com.newsaggregator.activityservice.service.ActivityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository repository;

    @Override
    public ActivityResponseDTO logActivity(ActivityRequestDTO dto) {
        // Prevent duplicate LIKEs from same user on same post
        if (dto.getActivityType() == ActivityType.LIKE) {
            boolean alreadyLiked = repository.findByUserIdAndNewsIdAndActivityType(
                    dto.getUserId(), dto.getNewsId(), ActivityType.LIKE).size() > 0;

            if (alreadyLiked) {
                throw new RuntimeException("User has already liked this news post.");
            }
        }

        Activity activity = Activity.builder()
                .userId(dto.getUserId())
                .newsId(dto.getNewsId())
                .activityType(dto.getActivityType())
                .comment(dto.getComment())
                .timestamp(LocalDateTime.now())
                .build();

        Activity saved = repository.save(activity);
        return mapToResponseDTO(saved);
    }

    @Override
    public List<ActivityResponseDTO> getActivitiesByUser(Long userId) {
        return repository.findByUserId(userId)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<ActivityResponseDTO> getActivitiesByNews(Long newsId) {
        return repository.findByNewsId(newsId)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public void removeLike(Long userId, Long newsId) {
        List<Activity> likeActivities = repository.findByUserIdAndNewsIdAndActivityType(
                userId, newsId, ActivityType.LIKE);

        if (!likeActivities.isEmpty()) {
            repository.deleteAll(likeActivities);  // Deletes all LIKEs from that user on this post
        }
    }

    private ActivityResponseDTO mapToResponseDTO(Activity activity) {
        return ActivityResponseDTO.builder()
                .id(activity.getId())
                .userId(activity.getUserId())
                .newsId(activity.getNewsId())
                .activityType(activity.getActivityType())
                .comment(activity.getComment())
                .timestamp(activity.getTimestamp())
                .build();
    }
}
