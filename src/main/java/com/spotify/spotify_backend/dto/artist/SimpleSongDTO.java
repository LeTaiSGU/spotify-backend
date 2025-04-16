package com.spotify.spotify_backend.dto.artist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SimpleSongDTO {
    private Long songId;
    private String songName;
}
