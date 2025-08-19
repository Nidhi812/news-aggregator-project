// UserProfile.java
package com.example.news.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String role;

    private String username;       // ✅ new field
    private String phone;          // ✅ new field
    private String profilePhoto;   // ✅ new field

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> interest;
}
