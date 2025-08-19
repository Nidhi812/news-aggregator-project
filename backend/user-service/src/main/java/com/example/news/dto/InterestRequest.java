package com.example.news.dto;

import java.util.List;

public class InterestRequest {
    private String email;
    private List<String> interests;

    public String getEmail() {
        return email;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
