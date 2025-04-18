package com.spotify.spotify_backend.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.spotify.spotify_backend.dto.artist.SimpleSongDTO;
import com.spotify.spotify_backend.dto.playlist.playlistDto;
import com.spotify.spotify_backend.dto.playlist.playlistResponse;
import com.spotify.spotify_backend.dto.playlist.playlistUp;
import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.model.Users;
import com.spotify.spotify_backend.repository.UserRepository;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    @Mapping(source = "userId", target = "user")
    Playlist toPlaylist(playlistDto dto, @Context UserRepository userRepository);

    default Users mapUser(Long userId, @Context UserRepository userRepository) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Mappings({
            @Mapping(target = "playlistId", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "coverImage", ignore = true)
    })
    void updatePlaylistFromDto(playlistUp dto, @MappingTarget Playlist playlist);

    @Mappings({
            @Mapping(target = "userId", source = "user.userId"),
            @Mapping(target = "songs", expression = "java(mapPlaylistSongs(playlist))")
    })
    playlistResponse toPlaylistResponse(Playlist playlist);

    default Set<SimpleSongDTO> mapPlaylistSongs(Playlist playlist) {
        if (playlist.getPlaylistSongs() == null) {
            return null;
        }
        return playlist.getPlaylistSongs().stream()
                .map(playlistSong -> SimpleSongDTO.builder()
                        .songId(playlistSong.getSong().getSongId())
                        .songName(playlistSong.getSong().getSongName())
                        .build())
                .collect(Collectors.toSet());
    }

}
