package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.artist.ArtistRequestDTO;
import com.spotify.spotify_backend.dto.artist.ArtistResponseDTO;
import com.spotify.spotify_backend.dto.artist.ArtistUpdateDTO;
import com.spotify.spotify_backend.dto.artist.SimpleSongDTO;
import com.spotify.spotify_backend.dto.search.artistDto;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.repository.SongRepository;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ArtistMapper {

    @Autowired
    protected SongRepository songRepository;

    @Mapping(target = "featuredSongs", source = "featuredSongs", qualifiedByName = "mapFeaturedSongs")
    public abstract ArtistResponseDTO toDTO(Artist artist);

    @Mapping(target = "artistId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "featuredSongs", ignore = true)
    public abstract Artist toArtist(ArtistRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "featuredSongs", ignore = true)
    public abstract void updateArtistFromDTO(ArtistUpdateDTO dto, @MappingTarget Artist artist);

    @AfterMapping
    protected void enrichWithOwnedSongs(Artist artist, @MappingTarget ArtistResponseDTO dto) {
        Set<SimpleSongDTO> ownedSongs = songRepository.findByArtist_ArtistIdAndStatus(artist.getArtistId(), true)
                .stream()
                .map(song -> new SimpleSongDTO(song.getSongId(), song.getSongName()))
                .collect(Collectors.toSet());
        dto.setSongs(ownedSongs); //

    }

    @Named("mapFeaturedSongs")
    protected static Set<SimpleSongDTO> mapFeaturedSongs(Set<Song> songs) {
        if (songs == null)
            return null;
        return songs.stream()
                .map(song -> new SimpleSongDTO(song.getSongId(), song.getSongName()))
                .collect(Collectors.toSet());
    }
}
