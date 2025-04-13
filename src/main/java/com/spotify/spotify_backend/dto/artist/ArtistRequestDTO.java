package com.spotify.spotify_backend.dto.artist;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;

import com.spotify.spotify_backend.model.Song;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
public class ArtistRequestDTO {

    private String name;
    private String img;
    private String description;
    private Boolean status; // Trạng thái nghệ sĩ (có thể là true hoặc false)
}
