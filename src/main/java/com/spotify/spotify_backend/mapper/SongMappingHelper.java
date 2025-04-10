package com.spotify.spotify_backend.mapper;

import org.springframework.stereotype.Component;

import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.repository.AlbumRepository;
import com.spotify.spotify_backend.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SongMappingHelper {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public Artist mapArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with ID: " + id));
        return artist;

    }

    public Album mapAlbum(Long id) {
        if (id == null)
            return null;
        return albumRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Không tìm thấy album với ID: " + id));
    }

    public Set<Artist> mapArtistIds(Set<Long> artistIds) {
        if (artistIds == null || artistIds.isEmpty()) {
            return Collections.emptySet();
        }
        return artistIds.stream()
                .map(id -> {
                    Artist artist = new Artist();
                    artist.setArtistId(id);
                    return artist;
                })
                .collect(Collectors.toSet());
    }

}
