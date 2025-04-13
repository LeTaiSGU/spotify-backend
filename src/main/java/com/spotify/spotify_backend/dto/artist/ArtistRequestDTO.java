package com.spotify.spotify_backend.dto.artist;

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
public class ArtistRequestDTO {

    private String name;
    private String img;
    private String description;

    @Builder.Default
    private Boolean status = true; // Trạng thái nghệ sĩ (có thể là true hoặc false)
}
