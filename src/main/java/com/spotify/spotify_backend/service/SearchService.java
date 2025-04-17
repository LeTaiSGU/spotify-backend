package com.spotify.spotify_backend.service;

import java.util.stream.Collectors;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify.spotify_backend.dto.searchResponse;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.mapper.SongMapper;
import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.repository.AlbumRepository;
import com.spotify.spotify_backend.repository.PlaylistRepository;
import com.spotify.spotify_backend.repository.SongRepository;

@Service
public class SearchService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    SongMapper songMapper;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    AlbumRepository albumRepository;

    public searchResponse searchKeyWord(String keyWord) {
        List<songResponse> songs = songRepository.findAll()
                .stream()
                .filter(song -> song.getSongName() != null
                        && song.getSongName().toLowerCase().contains(keyWord.toLowerCase()))
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .limit(4)
                .map(songMapper::toDto)
                .collect(Collectors.toList());

        List<Playlist> playlists = playlistRepository.findAll()
                .stream()
                .filter(playlist -> playlist.getName() != null
                        && playlist.getName().toLowerCase().contains(keyWord.toLowerCase()))
                .sorted((p1, p2) -> p2.getCreateAt().compareTo(p1.getCreateAt()))
                .limit(4)
                .collect(Collectors.toList());

        return searchResponse.builder()
                .songResult(songs)
                .playlists(playlists)
                .build();
    }

}
