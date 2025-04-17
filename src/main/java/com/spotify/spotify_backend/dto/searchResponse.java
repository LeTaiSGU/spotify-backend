package com.spotify.spotify_backend.dto;

import java.util.List;

import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.dto.artist.ArtistRequestDTO;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.model.Playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class searchResponse {
    List<songResponse> songResult;
    List<Playlist> playlists;
}
