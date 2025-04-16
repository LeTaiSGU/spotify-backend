package com.spotify.spotify_backend.dto.album;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumUpdateDTO {

    private Long albumId;
    private String title;
    private LocalDate releaseDate;
    private String coverImage;
    private Long artistId;
    private String type;
}
