package com.spotify.spotify_backend.dto.search;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class artistDto {
    private Long artistId;
    private String name;
    private String img;
    private String description;
    private Boolean status;
    private LocalDate createdAt;
}
