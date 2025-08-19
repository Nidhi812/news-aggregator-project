package com.newsaggregator.analyticsservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newsaggregator.analyticsservice.dto.AnalyticsRequestDTO;
import com.newsaggregator.analyticsservice.dto.AnalyticsResponseDTO;
// import com.newsaggregator.analyticsservice.model.Analytics;
import com.newsaggregator.analyticsservice.service.AnalyticsService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/record")
    public ResponseEntity<AnalyticsResponseDTO> record(@RequestBody AnalyticsRequestDTO request) {
        AnalyticsResponseDTO response = analyticsService.recordActivity(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnalyticsResponseDTO>> getAll() {
        return ResponseEntity.ok(analyticsService.getAllAnalytics());
    }
    // private final AnalyticsService analyticsService;

    // @PostMapping("/view/{category}")
    // public Analytics recordView(@PathVariable String category) {
    //     return analyticsService.recordView(category);
    // }

    // @PostMapping("/like/{category}")
    // public Analytics recordLike(@PathVariable String category) {
    //     return analyticsService.recordLike(category);
    // }

    // @PostMapping("/bookmark/{category}")
    // public Analytics recordBookmark(@PathVariable String category) {
    //     return analyticsService.recordBookmark(category);
    // }

    // @GetMapping("/stats")
    // public List<Analytics> getAllStats() {
    //     return analyticsService.getAllStats();
    // }
}
