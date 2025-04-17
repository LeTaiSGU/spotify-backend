package com.spotify.spotify_backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.spotify.spotify_backend.dto.album.AlbumRequestDTO;
import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.dto.album.AlbumUpdateDTO;
import com.spotify.spotify_backend.dto.search.albumDto;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.repository.ArtistRepository;

@Mapper(componentModel = "spring", uses = { ArtistMapper.class })
public interface AlbumMapper {
    // Chuyển đổi từ model Album sang AlbumResponseDTO
    @Mapping(source = "artist", target = "artist") // Tự map nguyên object
    AlbumResponseDTO toDTO(Album album);

    // Chuyển đổi từ AlbumRequestDTO sang model Album
    @Mapping(target = "albumId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "artistId", target = "artist")
    Album toAlbum(AlbumRequestDTO albumDTO, @Context ArtistRepository artistRepository);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "artistId", target = "artist")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateAlbumFromDTO(AlbumUpdateDTO albumUpdateDTO, @MappingTarget Album album,
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
