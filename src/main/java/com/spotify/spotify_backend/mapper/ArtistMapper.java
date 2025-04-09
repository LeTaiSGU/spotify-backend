package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.artist.artistdto;
import com.spotify.spotify_backend.model.Artist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    Artist toArtist(artistdto createArtistDTO);
}
