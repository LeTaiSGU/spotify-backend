package com.spotify.spotify_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.playlist.playlistAdminDTO;
import com.spotify.spotify_backend.dto.playlist.playlistDto;
import com.spotify.spotify_backend.dto.playlist.playlistResponse;
import com.spotify.spotify_backend.dto.playlist.playlistUp;
import com.spotify.spotify_backend.dto.playlist.PlaylistUpdateAdminDTO;
import com.spotify.spotify_backend.dto.playlistsong.showPlaylistSong;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.service.PlaylistService;
import com.spotify.spotify_backend.service.PlaylistSongService;

import lombok.extern.java.Log;

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

    // Get by page
    @GetMapping("/page")
    public ApiResponse<PageResponseDTO<Playlist>> getAllPlaylistsPaged(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "status", required = false) Boolean status) {

        Page<Playlist> page = playlistService.getAllPlaylistsPaged(pageNo, pageSize, sortBy, sortDir, status);

        PageResponseDTO<Playlist> response = PageResponseDTO.<Playlist>builder()
                .content(page.getContent())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();

        return ApiResponse.<PageResponseDTO<Playlist>>builder()
                .code(1000)
                .message("Lấy danh sách playlist thành công")
                .result(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Playlist> getPlaylistById(@PathVariable Long id) {
        ApiResponse<Playlist> apiResponse = new ApiResponse<>();
        Playlist playlist = playlistService.getPlaylistById(id);
        apiResponse.setResult(playlist);
        apiResponse.setCode(1000);
        apiResponse.setMessage("Playlist with id: " + id);
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
    public ApiResponse<Playlist> editPlaylist(@PathVariable Long id,
            @RequestPart("playlist") playlistUp updatePlaylist,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
        ApiResponse<Playlist> apiResponse = new ApiResponse<>();
        Playlist playlist = playlistService.editPlaylist(updatePlaylist, avatar, id);
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

    @PostMapping(value = "/adminCreate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Playlist> createPlaylistAdmin(
            @RequestPart("playlistDto") playlistAdminDTO newPlaylist,
            @RequestPart("coverImage") MultipartFile coverImage) {

        ApiResponse<Playlist> apiResponse = new ApiResponse<>();
        Playlist playlist = playlistService.createPlaylistAdmin(newPlaylist,
                coverImage);

        apiResponse.setMessage("Playlist created successfully");
        apiResponse.setResult(playlist);
        apiResponse.setCode(1000);
        return apiResponse;
    }

    @PutMapping(value = "/adminUpdate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Playlist> updatePlaylistAdmin(
            @RequestPart("playlistUpdateDto") PlaylistUpdateAdminDTO playlistUpdateDto,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        ApiResponse<Playlist> apiResponse = new ApiResponse<>();
        Playlist playlist = playlistService.updatePlaylistAdmin(playlistUpdateDto, coverImage);
        apiResponse.setResult(playlist);
        apiResponse.setMessage("Playlist updated successfully");
        return apiResponse;
    }

    // add Song on playlist
    @PostMapping("/addsong")
    public ApiResponse<showPlaylistSong> addSongToPlaylist(
            @RequestParam("playlist") Long playlistId,
            @RequestParam("song") Long songId) {
        ApiResponse<showPlaylistSong> apiResponse = new ApiResponse<>();
        try {
            playlistSongService.addSongToPlaylist(playlistId, songId);
            showPlaylistSong show = playlistSongService.getPlaylistSongsByPlaylistId(playlistId);
            apiResponse.setResult(show);
            apiResponse.setMessage("Song added to playlist successfully");
            apiResponse.setCode(200);
        } catch (AppException e) {
            apiResponse.setCode(e.getErrorCode().getCode());
            apiResponse.setMessage(e.getErrorCode().getMessage());
        } catch (DataIntegrityViolationException e) {
            apiResponse.setCode(ErrorCode.VALIDATION_ERROR.getCode());
            apiResponse.setMessage("Duplicate entry: " + e.getMessage());
        } catch (Exception e) {
            apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
            apiResponse.setMessage("Unexpected error: " + e.getMessage());
        }
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

    // disable playlist
    @PutMapping("disable/{id}")
    public ApiResponse<Playlist> deletePlaylist(@PathVariable Long id) {
        ApiResponse<Playlist> apiResponse = new ApiResponse<>();
        Playlist result = playlistService.deletePlaylist(id);
        apiResponse.setResult(result);
        apiResponse.setMessage("Delete successful!");
        return apiResponse;
    }

    // change private to playlist
    @PutMapping("private/{id}")
    public ApiResponse<Playlist> changePrivateStatus(@PathVariable Long id) {
        Playlist changePlaylist = playlistService.changePrivate(id);
        ApiResponse<Playlist> apiResponse = new ApiResponse<Playlist>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Playlist change status private succesfully");
        apiResponse.setResult(changePlaylist);
        return apiResponse;
    }
}