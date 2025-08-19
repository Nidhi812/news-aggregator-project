package com.example.news.service;

import com.example.news.dto.InterestRequest;
import com.example.news.dto.UpdateProfileRequest;
import com.example.news.model.UserProfile;
import com.example.news.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserProfileRepository userRepo;

    public UserProfile createuser(UserProfile user) {
        return userRepo.save(user);
    }

    public Optional<UserProfile> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public UserProfile updateInterests(InterestRequest request) {
        UserProfile user = userRepo.findByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("User not found with email: " + request.getEmail()));
        user.setInterest(request.getInterests());
        return userRepo.save(user);
    }

    public List<UserProfile> getUsersByRole(String role) {
        return userRepo.findByRole(role);
    }

    public UserProfile updateUserProfile(UpdateProfileRequest request) {
        UserProfile user = userRepo.findByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("User not found with email: " + request.getEmail()));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getProfilePhoto() != null) user.setProfilePhoto(request.getProfilePhoto());

        return userRepo.save(user);
    }
    public String saveProfilePhoto(MultipartFile file) {
    try {
        String uploadDir = "uploads/";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "http://localhost:8082/uploads/" + fileName;
    } catch (IOException e) {
        throw new RuntimeException("Failed to store file", e);
    }
}

}
