package com.newsaggregator.auth.service;

import com.newsaggregator.auth.dto.AuthResponse;
import com.newsaggregator.auth.dto.LoginRequest;
import com.newsaggregator.auth.dto.RegisterRequest;
import com.newsaggregator.auth.dto.UserProfileRequest;
import com.newsaggregator.auth.model.User;
import com.newsaggregator.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_SERVICE_URL = "http://localhost:8082/api/user-profiles/create";

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf(request.getRole()));
        user.setVerificationToken(UUID.randomUUID().toString());

        User savedUser = userRepository.save(user);

        // ✅ Send link to frontend, which will hit backend after confirmation
String verificationLink = "http://localhost:5173/verify.html?token=" + user.getVerificationToken();
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);

        // Optional: Create user profile in User Service
        try {
            UserProfileRequest profileRequest = new UserProfileRequest(
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
            );
            restTemplate.postForEntity(USER_SERVICE_URL, profileRequest, String.class);
            System.out.println("User profile creation request sent.");
        } catch (Exception e) {
            System.out.println("Failed to create user profile: " + e.getMessage());
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, savedUser.getId(), savedUser.getName(),
                savedUser.getEmail(), savedUser.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getName(),
                user.getEmail(), user.getRole().name());
    }

    public boolean verifyEmail(String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return true;
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public String extractEmailFromToken(String token) {
        return jwtService.extractEmail(token);
    }

    public String extractRoleFromToken(String token) {
        return jwtService.extractRole(token);
    }
}
