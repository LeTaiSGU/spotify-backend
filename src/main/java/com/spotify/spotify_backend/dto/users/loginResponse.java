package com.spotify.spotify_backend.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class loginResponse {
    boolean isAuthenicated;
    String accessToken; // JWT token
    String userName;
    String email;
    String role; // "USER" hoặc "ADMIN"
    String authProvider; // "LOCAL" hoặc "GOOGLE"
}
