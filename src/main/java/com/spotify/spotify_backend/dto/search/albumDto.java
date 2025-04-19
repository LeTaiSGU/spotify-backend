package com.spotify.spotify_backend.dto.search;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class albumDto {
    private Long albumId;
    private String title;
    private artistDto artist;
    private LocalDate releaseDate;
    private String coverImage;
    private String type;
    private LocalDate createdAt;
    private Boolean status;

}
