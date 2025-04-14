package com.spotify.spotify_backend.dto.playlistsong;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistSongDto {
    private LocalDateTime addedAt;
    private Long songId;
    private String songName;
    private String albumName;
    private Long duration;
}