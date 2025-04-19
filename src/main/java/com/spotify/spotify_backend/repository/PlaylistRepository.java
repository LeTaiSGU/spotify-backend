package com.spotify.spotify_backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotify.spotify_backend.model.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser_UserId(Long userId);

    List<Playlist> findByUser_UserIdAndStatusTrue(Long userId);

    Page<Playlist> findAllByStatus(Pageable pageable, Boolean status);

    Page<Playlist> findAll(Pageable pageable); // Added this method to handle the status filter
}
