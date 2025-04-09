package com.spotify.spotify_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artists")
public class Artist {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long artistId;

        private String name;
        private String img;
        private String description;
        private LocalDate created_at;

        @ManyToMany(mappedBy = "featuredArtists")
        private Set<Song> featuredSongs;
}
