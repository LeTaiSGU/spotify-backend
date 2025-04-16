package com.spotify.spotify_backend.repository;

import com.spotify.spotify_backend.model.Song;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Song findBySongName(String songName);

    boolean existsBySongName(String songName);

    boolean existsBySongId(Long id);

    Page<Song> findAllByStatus(Boolean status, Pageable pageable);

    List<Song> findByArtist_ArtistIdAndStatus(Long artistId, Boolean status);

}