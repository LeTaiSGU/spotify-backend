package com.spotify.spotify_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify.spotify_backend.dto.playlistsong.showPlaylistSong;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.PlaylistSongMapper;
import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.model.PlaylistSong;
import com.spotify.spotify_backend.repository.PlayListSongRepo;
import com.spotify.spotify_backend.repository.PlaylistRepository;
import com.spotify.spotify_backend.repository.SongRepository;

@Service
public class PlaylistSongService {

    @Autowired
    private PlayListSongRepo playListSongRepo;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    PlaylistSongMapper playlistSongMapper;

    @Autowired
    SongRepository songRepository;

    public List<PlaylistSong> getAllPlaylistSongs() {
        return playListSongRepo.findAll();
    }

    public showPlaylistSong getPlaylistSongsByPlaylistId(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        List<PlaylistSong> songs = playListSongRepo.findByPlaylist_PlaylistId(playlistId);
        showPlaylistSong dto = playlistSongMapper.toShowPlaylistSong(playlist, songs);
        return dto;
    }

    public PlaylistSong addSongToPlaylist(Long playlistId, Long songId) {
        PlaylistSong playlistSong = PlaylistSong.builder()
                .playlist(playlistRepository.findById(playlistId)
                        .orElseThrow(() -> new RuntimeException("Playlist not found")))
                .song(songRepository.findById(songId)
                        .orElseThrow(() -> new RuntimeException("Song not found")))
                .addedAt(java.time.LocalDateTime.now())
                .build();
        return playListSongRepo.save(playlistSong);
    }

    public PlaylistSong removeSongFromPlaylist(Long playlistId, Long songId) {
        PlaylistSong playlistSong = playListSongRepo.findByPlaylist_PlaylistIdAndSong_SongId(playlistId, songId);
        if (playlistSong != null) {
            playListSongRepo.delete(playlistSong);
            return playlistSong;
        } else {
            throw new RuntimeException("Playlist song not found");
        }
    }

}
