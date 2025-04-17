package com.spotify.spotify_backend.dto.search;

import java.util.List;

import com.spotify.spotify_backend.dto.playlistsong.showPlaylistSong;
import com.spotify.spotify_backend.dto.song.songResponse;
// import com.spotify.spotify_backend.model.Playlist;

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
    List<AlbumSearchDTO> albumResult;
    List<showPlaylistSong> playlists;
}
