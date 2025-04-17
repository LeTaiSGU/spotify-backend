package com.spotify.spotify_backend.dto.search;

import java.util.List;

import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.dto.song.songResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumSearchDTO {
    private albumDto album;
    private List<songResponse> songs;
}
