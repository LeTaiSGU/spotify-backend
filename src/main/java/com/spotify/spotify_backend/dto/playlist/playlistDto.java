package com.spotify.spotify_backend.dto.playlist;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class playlistDto {
    private String name;
    private Boolean isPrivate;
    private String coverImage;
    private String description;
    private Boolean statust = true;
    private LocalDateTime updateAt = LocalDateTime.now();
    private Long userId;
    private LocalDateTime createAt = LocalDateTime.now();
}
