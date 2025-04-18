package com.spotify.spotify_backend.repository;

import com.spotify.spotify_backend.model.Song;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // random song query
    @Query(value = "SELECT * FROM songs ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Song findRandomSong();

    @Query(value = "SELECT * FROM songs WHERE song_id <> :excludeId ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Song findRandomSongExclude(@Param("excludeId") Long excludeId);

    Song findBySongName(String songName);

    boolean existsBySongName(String songName);

    boolean existsBySongId(Long id);

    Page<Song> findAllByStatus(Boolean status, Pageable pageable);

    List<Song> findByAlbum_AlbumId(Long id);

    List<Song> findByArtist_ArtistIdAndStatus(Long artistId, Boolean status);
}