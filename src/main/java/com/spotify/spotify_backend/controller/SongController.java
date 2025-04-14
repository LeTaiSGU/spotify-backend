package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.dto.song.songUpdate;
import com.spotify.spotify_backend.dto.song.songdto;
// import com.spotify.spotify_backend.mapper.ArtistMappingHelper;
import com.spotify.spotify_backend.mapper.SongMapper;
// import com.spotify.spotify_backend.mapper.SongMappingHelper;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    // https://ytmp3.cc/Nnht/ link convert mp3

    @Autowired
    private SongService songService;

    @Autowired
    private SongMapper songMapper;

    @GetMapping()
    public ApiResponse<List<songResponse>> getAll() {
        ApiResponse<List<songResponse>> apiResponse = new ApiResponse<>();
        List<songResponse> songList = songService.getAllSongs().stream()
                .map(song -> songMapper.toDto(song))
                .toList();
        // List<Song> songList = songService.getAllSongs();
        apiResponse.setResult(songList);
        apiResponse.setCode(1000);
        apiResponse.setMessage("List of all songs");
        return apiResponse;
    }

    // upload song
    @PostMapping("/upload")
    public ApiResponse<songResponse> uploadSong(
            @RequestPart("song") songdto song,
            @RequestPart("songFile") MultipartFile songFile,
            @RequestPart("imgFile") MultipartFile imgFile) {

        Song savedSong = songService.uploadSong(song, songFile, imgFile);
        songResponse response = songMapper.toDto(savedSong);
        ApiResponse<songResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(response);
        return apiResponse;
    }

    @PutMapping(value = "/update/{songId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<songResponse> editSong(
            @PathVariable Long songId,
            @RequestPart("song") songUpdate song,
            @RequestPart(value = "songFile", required = false) MultipartFile songFile,
            @RequestPart(value = "imgFile", required = false) MultipartFile imgFile) {
        ApiResponse<songResponse> apiResponse = new ApiResponse<>();
        songResponse updatedSong = songService.updateSong(songId, song, songFile, imgFile);
        apiResponse.setResult(updatedSong);
        apiResponse.setMessage("Song updated successfully");
        return apiResponse;
    }

    @PutMapping("/status/{songId}")
    public ApiResponse<songResponse> disableSong(@PathVariable Long songId) {
        Song song = songService.disableSong(songId);
        songResponse response = songMapper.toDto(song);

        ApiResponse<songResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Song disabled successfully");
        apiResponse.setResult(response);
        return apiResponse;
    }
}
