package com.spotify.spotify_backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.spotify.spotify_backend.dto.playlistsong.PlaylistSongDto;
import com.spotify.spotify_backend.dto.playlistsong.createDto;
import com.spotify.spotify_backend.dto.playlistsong.showPlaylistSong;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.model.PlaylistSong;

@Mapper(componentModel = "spring")
public interface PlaylistSongMapper {
        @Mappings({
                        @Mapping(target = "playlist", expression = "java(helper.mapPlaylist(dto.getPlaylistId()))"),
                        @Mapping(target = "song", expression = "java(helper.mapSong(dto.getSongId()))"),
                        @Mapping(target = "addedAt", expression = "java(java.time.LocalDateTime.now())"),
                        @Mapping(target = "id", ignore = true)
        })
        PlaylistSong toPlaylistSong(createDto dto, @Context PlaylistSongHelper helper);

        @Mapping(target = "songId", source = "song.songId")
        @Mapping(target = "songName", source = "song.songName")
        @Mapping(target = "albumName", source = "song.album.title")
        @Mapping(target = "duration", source = "song.duration")
        PlaylistSongDto toDto(PlaylistSong playlistSong);

        default showPlaylistSong toShowPlaylistSong(Playlist playlist, List<PlaylistSong> playlistSongs) {
                List<songResponse> songDtos = playlistSongs.stream()
                                .filter(ps -> ps.getPlaylist().getPlaylistId().equals(playlist.getPlaylistId()))
                                .map(ps -> {
                                        songResponse response = new songResponse();
                                        response.setSongId(ps.getSong().getSongId());

                                        // Add null check for Album
                                        if (ps.getSong().getAlbum() != null) {
                                                response.setAlbumId(ps.getSong().getAlbum().getAlbumId());
                                        }

                                        // Add null check for Artist
                                        if (ps.getSong().getArtist() != null) {
                                                response.setArtistId(ps.getSong().getArtist().getArtistId());
                                                response.setArtistName(ps.getSong().getArtist().getName());
                                        }

                                        response.setSongName(ps.getSong().getSongName());
                                        response.setDuration(ps.getSong().getDuration());
                                        response.setFileUpload(ps.getSong().getFileUpload());
                                        response.setCreatedAt(ps.getSong().getCreatedAt());
                                        response.setStatus(ps.getSong().getStatus());
                                        response.setImg(ps.getSong().getImg());
                                        response.setDescription(ps.getSong().getDescription());
                                        return response;
                                })
                                .collect(Collectors.toList());

                return showPlaylistSong.builder()
                                .playlist(playlist)
                                .songs(songDtos)
                                .build();
        }
}
