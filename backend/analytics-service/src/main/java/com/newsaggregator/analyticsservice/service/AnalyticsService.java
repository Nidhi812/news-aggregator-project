package com.newsaggregator.analyticsservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.newsaggregator.analyticsservice.dto.AnalyticsRequestDTO;
import com.newsaggregator.analyticsservice.dto.AnalyticsResponseDTO;
import com.newsaggregator.analyticsservice.model.Analytics;
import com.newsaggregator.analyticsservice.repository.AnalyticsRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository repository;

    public AnalyticsResponseDTO recordActivity(AnalyticsRequestDTO request) {
        Analytics analytics = repository.findByCategory(request.getCategory())
                .orElse(Analytics.builder()
                        .category(request.getCategory())
                        .views(0)
                        .likes(0)
                        .bookmarks(0)
                        .build());

        switch (request.getType().toLowerCase()) {
            case "view" -> analytics.setViews(analytics.getViews() + 1);
            case "like" -> analytics.setLikes(analytics.getLikes() + 1);
            case "bookmark" -> analytics.setBookmarks(analytics.getBookmarks() + 1);
        }

        Analytics saved = repository.save(analytics);

        return AnalyticsResponseDTO.builder()
                .category(saved.getCategory())
                .views(saved.getViews())
                .likes(saved.getLikes())
                .bookmarks(saved.getBookmarks())
                .build();
    }

    public List<AnalyticsResponseDTO> getAllAnalytics() {
        return repository.findAll().stream().map(a -> AnalyticsResponseDTO.builder()
                .category(a.getCategory())
                .views(a.getViews())
                .likes(a.getLikes())
                .bookmarks(a.getBookmarks())
                .build()
        ).collect(Collectors.toList());
    }

    // private final AnalyticsRepository repository;

    // public Analytics recordView(String category) {
    //     Analytics analytics = repository.findByCategory(category)
    //             .orElse(Analytics.builder().category(category).build());
    //     analytics.setViews(analytics.getViews() + 1);
    //     return repository.save(analytics);
    // }

    // public Analytics recordLike(String category) {
    //     Analytics analytics = repository.findByCategory(category)
    //             .orElse(Analytics.builder().category(category).build());
    //     analytics.setLikes(analytics.getLikes() + 1);
    //     return repository.save(analytics);
    // }

    // public Analytics recordBookmark(String category) {
    //     Analytics analytics = repository.findByCategory(category)
    //             .orElse(Analytics.builder().category(category).build());
    //     analytics.setBookmarks(analytics.getBookmarks() + 1);
    //     return repository.save(analytics);
    // }

    // public List<Analytics> getAllStats() {
    //     return repository.findAll();
    // }


}

