// UserProfileController.java
package com.example.news.controller;

import com.example.news.model.UserProfile;
import com.example.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public UserProfile createUserProfile(@RequestBody UserProfile userProfile) {
        return userService.createuser(userProfile);
    }
}
