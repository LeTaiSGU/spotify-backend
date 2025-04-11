package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.artist.artistResponse;
import com.spotify.spotify_backend.dto.artist.artistdto;
import com.spotify.spotify_backend.model.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

    // ReponseDTO toDTO(Artist artist);

    Artist toArtist(artistdto createArtistDTO);

    artistResponse toArtistResponse(Artist artist);
}
