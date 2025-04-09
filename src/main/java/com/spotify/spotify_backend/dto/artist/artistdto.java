package com.spotify.spotify_backend.dto.artist;

import com.spotify.spotify_backend.model.Song;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class artistdto {
    private String name;
    private String description;
    private LocalDate created_at = LocalDate.now();
//    private String img;
//    @ManyToMany(mappedBy = "featuredArtists")
//    private Set<Song> featuredSongs;
}
