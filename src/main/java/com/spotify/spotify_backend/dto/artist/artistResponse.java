package com.spotify.spotify_backend.dto.artist;

import java.time.LocalDate;
import lombok.Data;

@Data
public class artistResponse {
    private Long artistId;
    private String name;
    private String img;
    private String description;
    private LocalDate createdAt;
}
