package com.spotify.spotify_backend.dto.playlistsong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class createDto {
    private Long playlistId;
    private Long songId;
}
