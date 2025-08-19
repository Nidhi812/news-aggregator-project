package com.example.news.controller;

import org.springframework.web.multipart.MultipartFile;
import com.example.news.dto.InterestRequest;
import com.example.news.dto.UpdateProfileRequest;
import com.example.news.model.UserProfile;
import com.example.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Base test route
    @GetMapping
    public String userRoot() {
        return "🎯 User API is reachable. Use /create, /{email}, /update-profile, or /interests.";
    }

    // ✅ Create user
    @PostMapping("/create")
    public ResponseEntity<UserProfile> createUser(@RequestBody UserProfile user) {
        return ResponseEntity.ok(userService.createuser(user));
    }

    // ✅ Get user by email
    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Update interests
    @PostMapping("/interests")
    public ResponseEntity<?> updateInterests(@RequestBody InterestRequest request) {
        return ResponseEntity.ok(userService.updateInterests(request));
    }

    // ✅ Get all users by role
    @GetMapping("/all-users")
    public ResponseEntity<List<UserProfile>> getAllUsersByRoleUser() {
        return ResponseEntity.ok(userService.getUsersByRole("USER"));
    }

    // ✅ Update user profile (name, username, phone, profilePhoto)
    @PutMapping(value = "/update-profile", consumes = "multipart/form-data")
public ResponseEntity<UserProfile> updateUserProfile(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("username") String username,
        @RequestParam("phone") String phone,
        @RequestParam(value = "photo", required = false) MultipartFile photo) {

    String photoUrl = null;
    if (photo != null && !photo.isEmpty()) {
        photoUrl = userService.saveProfilePhoto(photo);
    }

    UpdateProfileRequest request = new UpdateProfileRequest();
    request.setEmail(email);
    request.setName(name);
    request.setUsername(username);
    request.setPhone(phone);
    request.setProfilePhoto(photoUrl); // we store only the URL

    return ResponseEntity.ok(userService.updateUserProfile(request));
}

}
