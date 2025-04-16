package com.spotify.spotify_backend.dto.playlistsong;

import java.util.List;

import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.model.Playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class showPlaylistSong {
    Playlist playlist;
    List<songResponse> songs;
}
