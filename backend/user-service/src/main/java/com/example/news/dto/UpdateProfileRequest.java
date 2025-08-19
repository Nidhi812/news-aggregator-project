package com.example.news.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email;
    private String name;
    private String username;
    private String phone;
    private String profilePhoto;
}
