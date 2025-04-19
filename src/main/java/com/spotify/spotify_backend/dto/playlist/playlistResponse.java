package com.spotify.spotify_backend.dto.playlist;

import java.time.LocalDateTime;
import java.util.Set;

import com.spotify.spotify_backend.dto.artist.SimpleSongDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class playlistResponse {
    private Long playlistId;
    private Long userId;
    private String name;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Boolean status;
    private Boolean isPrivate;
    private String coverImage;
    private Set<SimpleSongDTO> songs;
}