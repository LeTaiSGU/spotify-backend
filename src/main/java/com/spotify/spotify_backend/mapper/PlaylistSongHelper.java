package com.spotify.spotify_backend.mapper;

import org.springframework.stereotype.Component;

import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.repository.PlaylistRepository;
import com.spotify.spotify_backend.repository.SongRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaylistSongHelper {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    public Playlist mapPlaylist(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy playlist với ID: " + id));
    }

    public Song mapSong(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát với ID: " + id));
    }
}