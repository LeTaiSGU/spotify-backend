package com.spotify.spotify_backend.service;

import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify.spotify_backend.dto.playlistsong.showPlaylistSong;
import com.spotify.spotify_backend.dto.search.AlbumSearchDTO;
import com.spotify.spotify_backend.dto.search.albumDto;
import com.spotify.spotify_backend.dto.search.artistDto;
import com.spotify.spotify_backend.dto.search.searchResponse;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.mapper.AlbumMapper;
import com.spotify.spotify_backend.mapper.SongMapper;
import com.spotify.spotify_backend.model.PlaylistSong;
import com.spotify.spotify_backend.repository.AlbumRepository;
import com.spotify.spotify_backend.repository.PlaylistRepository;
import com.spotify.spotify_backend.repository.SongRepository;

@Service
public class SearchService {

        @Autowired
        private SongRepository songRepository;

        @Autowired
        private SongMapper songMapper;

        @Autowired
        private AlbumMapper albumMapper;

        @Autowired
        private PlaylistRepository playlistRepository;

        @Autowired
        private AlbumRepository albumRepository;

        public searchResponse searchKeyWord(String keyWord) {
                // Normalize keyword to lowercase for case-insensitive search
                String normalizedKeyWord = keyWord != null ? keyWord.toLowerCase() : "";

                // Search songs
                List<songResponse> songs = songRepository.findAll()
                                .stream()
                                .filter(song -> song.getSongName() != null
                                                && song.getSongName().toLowerCase().contains(normalizedKeyWord))
                                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                                .limit(4)
                                .map(songMapper::toDto)
                                .collect(Collectors.toList());

                // Search playlists
                List<showPlaylistSong> playlists = playlistRepository.findAll()
                                .stream()
                                .filter(playlist -> playlist.getName() != null
                                                && playlist.getName().toLowerCase().contains(normalizedKeyWord)
                                                && !playlist.getIsPrivate() && playlist.getStatus())
                                // Add this condition to filter out private
                                // playlists
                                .sorted((p1, p2) -> p2.getCreateAt().compareTo(p1.getCreateAt()))
                                .limit(4)
                                .map(playlist -> showPlaylistSong.builder()
                                                .playlist(playlist)
                                                .songs(
                                                                playlist.getPlaylistSongs()
                                                                                .stream()
                                                                                .map(PlaylistSong::getSong)
                                                                                .map(songMapper::toDto)
                                                                                .collect(Collectors.toList()))
                                                .build())
                                .collect(Collectors.toList());

                // Search albums
                List<AlbumSearchDTO> albums = albumRepository.findAll()
                                .stream()
                                .filter(album -> {
                                        String title = album.getTitle() != null ? album.getTitle().toLowerCase() : "";
                                        return title.contains(normalizedKeyWord);
                                })
                                .sorted((a1, a2) -> a2.getReleaseDate().compareTo(a1.getReleaseDate()))
                                .limit(4)
                                .map(album -> {
                                        artistDto mappedArtist = artistDto.builder()
                                                        .artistId(album.getArtist().getArtistId())
                                                        .name(album.getArtist().getName())
                                                        .img(album.getArtist().getImg())
                                                        .description(album.getArtist().getDescription())
                                                        .status(album.getArtist().getStatus())
                                                        .createdAt(album.getArtist().getCreatedAt())
                                                        .build();

                                        albumDto mappedAlbum = albumDto.builder()
                                                        .albumId(album.getAlbumId())
                                                        .title(album.getTitle())
                                                        .artist(mappedArtist)
                                                        .releaseDate(album.getReleaseDate())
                                                        .coverImage(album.getCoverImage())
                                                        .type(album.getType())
                                                        .createdAt(album.getCreatedAt())
                                                        .status(album.getStatus())
                                                        .build();

                                        List<songResponse> albumSongs = songRepository.findAll()
                                                        .stream()
                                                        .filter(song -> song.getAlbum() != null
                                                                        && song.getAlbum().getAlbumId()
                                                                                        .equals(album.getAlbumId()))
                                                        .map(songMapper::toDto)
                                                        .collect(Collectors.toList());

                                        return AlbumSearchDTO.builder()
                                                        .album(mappedAlbum)
                                                        .songs(albumSongs)
                                                        .build();
                                })
                                .collect(Collectors.toList());

                return searchResponse.builder()
                                .songResult(songs)
                                .playlists(playlists)
                                .albumResult(albums)
                                .build();
        }
}