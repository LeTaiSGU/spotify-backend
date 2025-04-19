package com.spotify.spotify_backend.dto.song;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class simpleArtistDto {
    private Long artistId;
    private String name;
}
