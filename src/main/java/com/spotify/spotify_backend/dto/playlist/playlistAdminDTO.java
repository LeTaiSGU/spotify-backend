package com.spotify.spotify_backend.dto.playlist;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class playlistAdminDTO {
    private String name;
    private Boolean isPrivate;
    private String coverImage;
    private String description;
    private Long userId;
    private List<Long> playlistSongIds;
}