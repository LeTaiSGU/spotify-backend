package com.spotify.spotify_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "albums")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumId;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = true)
    private Artist artist;

    private String title;
    private LocalDate releaseDate;
    private String description;

    private String coverImage;
    private String type; // EP or Album
    private LocalDateTime updatedAt;
}
