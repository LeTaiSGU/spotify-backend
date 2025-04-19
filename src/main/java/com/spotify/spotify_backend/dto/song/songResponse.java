package com.spotify.spotify_backend.dto.song;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class songResponse {
    private Long songId;
    private Long albumId;
    private String title;
    private Long artistId;
    private String artistName;
    private String songName;
    private Long duration;
    private String fileUpload;
    private LocalDate createdAt;
    private Boolean status;
    private String img;
    private String description;
    private Set<simpleArtistDto> featuredArtists;
}