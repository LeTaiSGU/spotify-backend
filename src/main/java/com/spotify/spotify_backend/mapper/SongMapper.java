package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.song.simpleArtistDto;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.dto.song.songUpdate;
import com.spotify.spotify_backend.dto.song.songdto;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Context;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SongMapper {

        @Mappings({
                        @Mapping(target = "artist", expression = "java(helper.mapArtist(songDto.getArtist_id()))"),
                        @Mapping(target = "album", expression = "java(helper.mapAlbum(songDto.getAlbum_id()))"),
                        @Mapping(target = "featuredArtists", expression = "java(helper.mapArtistIds(songDto.getFeaturedArtistIds()))")
        })
        Song toSong(songdto songDto, @Context SongMappingHelper helper);

        @Mappings({
                        @Mapping(target = "artistId", source = "artist.artistId"),
                        @Mapping(target = "artistName", source = "artist.name"),
                        @Mapping(target = "albumId", source = "album.albumId"),
                        @Mapping(target = "featuredArtists", expression = "java(mapFeaturedArtists(song.getFeaturedArtists()))")
        })
        songResponse toDto(Song song);

        default Set<Long> mapArtistIds(Set<Artist> artists) {
                if (artists == null) {
                        return null;
                }
                return artists.stream()
                                .map(Artist::getArtistId)
                                .collect(Collectors.toSet());
        }

        default Set<simpleArtistDto> mapFeaturedArtists(Set<Artist> artists) {
                if (artists == null) {
                        return null;
                }
                return artists.stream()
                                .map(artist -> simpleArtistDto.builder()
                                                .artistId(artist.getArtistId())
                                                .name(artist.getName())
                                                .build())
                                .collect(Collectors.toSet());
        }

        // update
        @Mappings({
                        @Mapping(target = "artist", expression = "java(helper.mapArtist(songUpdate.getArtist_id()))"),
                        @Mapping(target = "album", expression = "java(helper.mapAlbum(songUpdate.getAlbum_id()))"),
                        @Mapping(target = "featuredArtists", expression = "java(helper.mapArtistIds(songUpdate.getFeaturedArtistIds()))"),
                        @Mapping(target = "songId", ignore = true),
                        @Mapping(target = "fileUpload", ignore = true),
                        @Mapping(target = "img", ignore = true),
                        @Mapping(target = "createdAt", ignore = true),
                        @Mapping(target = "status", ignore = true)
        })
        void updateSongFromDto(songUpdate songUpdate, @MappingTarget Song song, @Context SongMappingHelper helper);
}
