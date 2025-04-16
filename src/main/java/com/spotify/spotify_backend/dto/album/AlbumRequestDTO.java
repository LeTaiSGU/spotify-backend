package com.spotify.spotify_backend.dto.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequestDTO {

    private String title;
    private LocalDate releaseDate;
    private String coverImage;
    private Long artistId;
    private String type;
    @Builder.Default
    private Boolean status = true;
}
