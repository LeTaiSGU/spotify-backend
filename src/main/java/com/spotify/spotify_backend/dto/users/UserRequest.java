package com.spotify.spotify_backend.dto.users;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
public class UserRequest {
    private String username;
    private String password;
}
