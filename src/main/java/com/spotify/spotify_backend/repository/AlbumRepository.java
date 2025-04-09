package com.spotify.spotify_backend.repository;

import com.spotify.spotify_backend.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Custom query methods can be defined here if needed
    // For example, find albums by artist, genre, etc.
}
