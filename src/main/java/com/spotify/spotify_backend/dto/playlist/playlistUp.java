package com.spotify.spotify_backend.dto.playlist;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class playlistUp {
    private String name;
    private Boolean isPrivate;
    private LocalDateTime updateAt = LocalDateTime.now();
    private String description;
    private Boolean status;
}
