package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.dto.song.songdto;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-11T00:15:45+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class SongMapperImpl implements SongMapper {

    @Override
    public Song toSong(songdto songDto, SongMappingHelper helper) {
        if ( songDto == null ) {
            return null;
        }

        Song.SongBuilder song = Song.builder();

        song.description( songDto.getDescription() );
        if ( songDto.getDuration() != null ) {
            song.duration( songDto.getDuration() );
        }
        song.songName( songDto.getSongName() );
        song.status( songDto.isStatus() );

        song.artist( helper.mapArtist(songDto.getArtist_id()) );
        song.album( helper.mapAlbum(songDto.getAlbum_id()) );
        song.featuredArtists( helper.mapArtistIds(songDto.getFeaturedArtistIds()) );

        return song.build();
    }

    @Override
    public songResponse toDto(Song song) {
        if ( song == null ) {
            return null;
        }

        songResponse songResponse = new songResponse();

        songResponse.setArtistId( songArtistArtistId( song ) );
        songResponse.setAlbumId( songAlbumAlbumId( song ) );
        songResponse.setCreatedAt( song.getCreatedAt() );
        songResponse.setDescription( song.getDescription() );
        songResponse.setDuration( (int) song.getDuration() );
        songResponse.setFileUpload( song.getFileUpload() );
        songResponse.setImg( song.getImg() );
        songResponse.setSongId( song.getSongId() );
        songResponse.setSongName( song.getSongName() );
        songResponse.setStatus( song.getStatus() );

        songResponse.setArtists_id( mapArtistIds(song.getFeaturedArtists()) );

        return songResponse;
    }

    private Long songArtistArtistId(Song song) {
        Artist artist = song.getArtist();
        if ( artist == null ) {
            return null;
        }
        return artist.getArtistId();
    }

    private Long songAlbumAlbumId(Song song) {
        Album album = song.getAlbum();
        if ( album == null ) {
            return null;
        }
        return album.getAlbumId();
    }
}
