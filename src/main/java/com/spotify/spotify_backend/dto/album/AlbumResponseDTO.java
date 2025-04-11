package com.spotify.spotify_backend.dto.album;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AlbumResponseDTO {
    private Long albumId;
    private Long artistId;
    private String title;
    private LocalDate releaseDate;
    private String description;
    private String coverImage;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
