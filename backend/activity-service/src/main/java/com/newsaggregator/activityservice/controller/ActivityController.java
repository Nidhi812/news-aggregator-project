package com.newsaggregator.activityservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newsaggregator.activityservice.dto.ActivityRequestDTO;
import com.newsaggregator.activityservice.dto.ActivityResponseDTO;
import com.newsaggregator.activityservice.service.ActivityService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ActivityResponseDTO logActivity(@RequestBody ActivityRequestDTO dto) {
        return activityService.logActivity(dto);
    }

    @GetMapping("/user/{userId}")
    public List<ActivityResponseDTO> getByUser(@PathVariable Long userId) {
        return activityService.getActivitiesByUser(userId);
    }

    @GetMapping("/news/{newsId}")
    public List<ActivityResponseDTO> getByNews(@PathVariable Long newsId) {
        return activityService.getActivitiesByNews(newsId);
    }
    @DeleteMapping("/unlike")
public ResponseEntity<?> unlike(@RequestParam Long userId, @RequestParam Long newsId) {
    activityService.removeLike(userId, newsId);
    return ResponseEntity.ok("Unliked");
}

}
