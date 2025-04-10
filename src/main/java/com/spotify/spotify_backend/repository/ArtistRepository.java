package com.spotify.spotify_backend.repository;

import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    // Custom query methods can be defined here if needed
    // For example, find artists by genre, name, etc
    Artist findByName(String name);

    boolean existsByName(String name);
}
