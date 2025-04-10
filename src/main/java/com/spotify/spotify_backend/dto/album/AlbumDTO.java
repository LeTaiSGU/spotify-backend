package com.spotify.spotify_backend.dto.album;

import java.time.LocalDate;
import com.spotify.spotify_backend.model.Artist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumDTO {
    private Long albumId;
    private Artist artist;
    private String title;
    private LocalDate releaseDate;
    private String description;
    private String coverImage;
    private String type; // EP or Album
}
