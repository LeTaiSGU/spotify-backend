package com.spotify.spotify_backend.dto.users;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String fullName;
    private String email;
    private LocalDate dob;
    private boolean isPremium;
    private String role; // "USER" hoặc "ADMIN"
    private String authProvider; // "LOCAL" hoặc "GOOGLE"
    private String avatar; // URL của ảnh đại diện
    private Boolean status; // true: active, false: inactive
}
