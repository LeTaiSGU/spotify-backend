package com.spotify.spotify_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    private String songName;
    private LocalDateTime duration; // Thời gian bài hát

    // Nghệ sĩ chính
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToMany
    @JoinTable(
            name = "song_featured_artists",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> featuredArtists;

    private String img;
    private String fileUpload;
    private String description;

    private LocalDateTime createdAt;
}
