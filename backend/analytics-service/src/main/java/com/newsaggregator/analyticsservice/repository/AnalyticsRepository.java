package com.newsaggregator.analyticsservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsaggregator.analyticsservice.model.Analytics;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    Optional<Analytics> findByCategory(String category);
}
