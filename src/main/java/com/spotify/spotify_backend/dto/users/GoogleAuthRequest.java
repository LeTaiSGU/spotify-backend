package com.spotify.spotify_backend.dto.users;

import lombok.Data;

@Data
public class GoogleAuthRequest {
    private String accessToken;
}
