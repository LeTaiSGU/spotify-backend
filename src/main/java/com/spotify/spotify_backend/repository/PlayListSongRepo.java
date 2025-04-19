package com.spotify.spotify_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotify.spotify_backend.model.PlaylistSong;

@Repository
public interface PlayListSongRepo extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylist_PlaylistId(Long playlistId);

    PlaylistSong findBySong_SongId(Long songId);

    PlaylistSong findByPlaylist_PlaylistIdAndSong_SongId(Long playlistId, Long songId);
}
