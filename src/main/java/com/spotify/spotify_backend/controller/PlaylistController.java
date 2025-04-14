package com.spotify.spotify_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.playlist.playlistDto;
import com.spotify.spotify_backend.dto.playlist.playlistUp;
import com.spotify.spotify_backend.dto.playlistsong.showPlaylistSong;
import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.service.PlaylistService;
import com.spotify.spotify_backend.service.PlaylistSongService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistSongService playlistSongService;

    @GetMapping()
    public ApiResponse<List<Playlist>> getAllPlaylist() {
        ApiResponse<List<Playlist>> apiResponse = new ApiResponse<>();
        List<Playlist> playlistList = playlistService.getAllPlaylists();
        apiResponse.setResult(playlistList);
        apiResponse.setCode(1000);
        apiResponse.setMessage("List of all playlists");
        return apiResponse;
    }

    @PostMapping("/create")
    public ApiResponse<Playlist> createPlaylist(@RequestBody playlistDto newPlaylist) {
        ApiResponse<Playlist> apiResponse = new ApiResponse<>();
        Playlist playlist = playlistService.createPlaylist(newPlaylist);
        apiResponse.setMessage("Playlist created successfully");
        apiResponse.setResult(playlist);
        apiResponse.setCode(1000);
        return apiResponse;
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Playlist>> getPlaylistsByUserId(@PathVariable Long userId) {
        ApiResponse<List<Playlist>> apiResponse = new ApiResponse<>();
        List<Playlist> playlistList = playlistService.getPlaylistsByUserId(userId);
        apiResponse.setResult(playlistList);
        apiResponse.setCode(1000);
        apiResponse.setMessage("List of playlists by user id: " + userId);
        return apiResponse;
    }

    @PutMapping("update/{id}")
    public ApiResponse<Playlist> editPlaylist(@PathVariable String id,
            @RequestPart("playlist") playlistUp updatePlaylist,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
        ApiResponse<Playlist> apiResponse = new ApiResponse<>();
        Playlist playlist = playlistService.editPlaylist(updatePlaylist, avatar, Long.parseLong(id));
        apiResponse.setResult(playlist);
        apiResponse.setMessage("Playlist updated successfully");
        return apiResponse;
    }

    // Get Song on playlist
    @GetMapping("/song/{playlistId}")
    public ApiResponse<showPlaylistSong> getMethodName(@PathVariable Long playlistId) {
        ApiResponse<showPlaylistSong> apiResponse = new ApiResponse<>();
        showPlaylistSong show = playlistSongService.getPlaylistSongsByPlaylistId(playlistId);
        apiResponse.setResult(show);
        apiResponse.setMessage("Song of playlist");
        return apiResponse;
    }

    // add Song on playlist
    @PostMapping("/addsong")
    public ApiResponse<showPlaylistSong> addSongToPlaylist(
            @RequestParam("playlist") Long playlistId,
            @RequestParam("song") Long songId) {
        ApiResponse<showPlaylistSong> apiResponse = new ApiResponse<>();
        playlistSongService.addSongToPlaylist(playlistId, songId);
        showPlaylistSong show = playlistSongService.getPlaylistSongsByPlaylistId(playlistId);
        apiResponse.setResult(show);
        apiResponse.setMessage("Song added to playlist successfully");
        return apiResponse;
    }

    // remove Song on playlist
    @PutMapping("/removesong")
    public ApiResponse<showPlaylistSong> removeSongFromPlaylist(
            @RequestParam("playlist") Long playlistId,
            @RequestParam("song") Long songId) {
        ApiResponse<showPlaylistSong> apiResponse = new ApiResponse<>();
        playlistSongService.removeSongFromPlaylist(playlistId, songId);
        showPlaylistSong show = playlistSongService.getPlaylistSongsByPlaylistId(playlistId);
        apiResponse.setResult(show);
        apiResponse.setMessage("Song removed from playlist successfully");
        return apiResponse;
    }

    @PutMapping("delete/{id}")
    public ApiResponse<String> deletePlaylist(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String result = playlistService.deletePlaylist(id);
        apiResponse.setResult(result);
        apiResponse.setMessage("Delete successful!");
        return apiResponse;
    }
}