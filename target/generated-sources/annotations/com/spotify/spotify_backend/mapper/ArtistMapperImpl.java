package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.artist.artistResponse;
import com.spotify.spotify_backend.dto.artist.artistdto;
import com.spotify.spotify_backend.model.Artist;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-11T00:16:58+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class ArtistMapperImpl implements ArtistMapper {

    @Override
    public Artist toArtist(artistdto createArtistDTO) {
        if ( createArtistDTO == null ) {
            return null;
        }

        Artist.ArtistBuilder artist = Artist.builder();

        artist.created_at( createArtistDTO.getCreated_at() );
        artist.description( createArtistDTO.getDescription() );
        artist.name( createArtistDTO.getName() );

        return artist.build();
    }

    @Override
    public artistResponse toArtistResponse(Artist artist) {
        if ( artist == null ) {
            return null;
        }

        artistResponse artistResponse = new artistResponse();

        artistResponse.setArtistId( artist.getArtistId() );
        artistResponse.setDescription( artist.getDescription() );
        artistResponse.setImg( artist.getImg() );
        artistResponse.setName( artist.getName() );

        return artistResponse;
    }
}
