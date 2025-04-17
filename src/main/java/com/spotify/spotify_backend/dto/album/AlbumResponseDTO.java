package com.spotify.spotify_backend.dto.album;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.spotify.spotify_backend.dto.artist.ArtistResponseDTO;

@Getter
@Setter
@Builder
public class AlbumResponseDTO {
    private Long albumId;
    private ArtistResponseDTO artist;
    private String title;
    private LocalDate releaseDate;
    private String coverImage;
    private String type;
    private LocalDate createdAt;
    private Boolean status;
}
