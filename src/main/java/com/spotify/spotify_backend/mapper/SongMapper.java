package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.dto.song.songdto;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Context;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mappings({
            @Mapping(target = "artist", expression = "java(helper.mapArtist(songDto.getArtist_id()))"),
            @Mapping(target = "album", expression = "java(helper.mapAlbum(songDto.getAlbum_id()))"),
            @Mapping(target = "featuredArtists", expression = "java(helper.mapArtistIds(songDto.getFeaturedArtistIds()))")
    })
    Song toSong(songdto songDto, @Context SongMappingHelper helper);

    @Mapping(target = "artistId", source = "artist.artistId")
    @Mapping(target = "albumId", source = "album.albumId")
    @Mapping(target = "artists_id", expression = "java(mapArtistIds(song.getFeaturedArtists()))")
    songResponse toDto(Song song);

    // Hàm này sẽ map Set<Artist> sang Set<Long>
    default Set<Long> mapArtistIds(Set<Artist> artists) {
        if (artists == null) {
            return null;
        }
        return artists.stream()
                .map(Artist::getArtistId)
                .collect(Collectors.toSet());
    }
}
