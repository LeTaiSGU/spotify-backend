package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.artist.ArtistRequestDTO;
import com.spotify.spotify_backend.dto.artist.ArtistResponseDTO;
import com.spotify.spotify_backend.dto.artist.ArtistUpdateDTO;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;

import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    // Map từ Entity → DTO
    @Mapping(target = "songIds", expression = "java(mapSongsToIds(artist.getSongs()))")
    @Mapping(target = "featuredSongIds", expression = "java(mapSongsToIds(artist.getFeaturedSongs()))")

    ArtistResponseDTO toDTO(Artist artist);

    // Map từ RequestDTO → Entity khi tạo mới
    @Mapping(target = "artistId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "featuredSongs", ignore = true)
    Artist toArtist(ArtistRequestDTO dto);

    // Update từ UpdateDTO → Entity hiện tại
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "featuredSongs", ignore = true)
    void updateArtistFromDTO(ArtistUpdateDTO dto, @MappingTarget Artist artist);

    // Convert Song List → ID List (để trả về client)
    default Set<Long> mapSongsToIds(Set<com.spotify.spotify_backend.model.Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return Set.of(); // <- Trả về empty set thay vì null
        }
        return songs.stream()
                .map(com.spotify.spotify_backend.model.Song::getSongId)
                .collect(Collectors.toSet());
    }

}
