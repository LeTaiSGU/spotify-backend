package com.spotify.spotify_backend.dto.artist;

import jakarta.validation.constraints.NotBlank;
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
public class ArtistUpdateDTO {
    private Long artistId;
    private String name;
    private String img;
    private String description;
}
