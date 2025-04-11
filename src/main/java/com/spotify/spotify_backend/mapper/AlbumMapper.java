package com.spotify.spotify_backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.spotify.spotify_backend.dto.album.AlbumRequestDTO;
import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.repository.ArtistRepository;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    AlbumMapper INSTANCE = Mappers.getMapper(AlbumMapper.class);

    @Mapping(source = "artist.artistId", target = "artistId")
    AlbumResponseDTO toDTO(Album album);

    @Mapping(target = "albumId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "artistId", target = "artist")
    Album toAlbum(AlbumRequestDTO albumDTO, @Context ArtistRepository artistRepository);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAlbumFromDTO(AlbumRequestDTO albumDTO, @MappingTarget Album album,
            @Context ArtistRepository artistRepository);

    // Default method để map artistId thành Artist
    default Artist mapArtistIdToArtist(Long artistId, @Context ArtistRepository artistRepository) {
        if (artistId == null) {
            return null;
        }
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
