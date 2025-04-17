package com.spotify.spotify_backend.dto.artist;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class ArtistResponseDTO {
    private Long artistId;
    private String name;
    private String img;
    private String description;
    private Boolean status;
    private LocalDate createdAt;

    private Set<SimpleSongDTO> songs; // Danh sách bài hát của nghệ sĩ
    private Set<SimpleSongDTO> featuredSongs;
}
