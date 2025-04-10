package com.spotify.spotify_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.spotify.spotify_backend.dto.album.AlbumDTO;
import com.spotify.spotify_backend.model.Album;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    AlbumMapper INSTANCE = Mappers.getMapper(AlbumMapper.class);

    AlbumDTO toDto(Album album);

    Album toEntity(AlbumDTO albumDTO);
}
