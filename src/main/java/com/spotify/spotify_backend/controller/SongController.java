package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.dto.song.songdto;
// import com.spotify.spotify_backend.mapper.ArtistMappingHelper;
import com.spotify.spotify_backend.mapper.SongMapper;
// import com.spotify.spotify_backend.mapper.SongMappingHelper;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

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
        apiResponse.setResult(songList);
        apiResponse.setCode(1000);
        apiResponse.setMessage("List of all songs");
        return apiResponse;
    }

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

    @PutMapping("/disable/{songId}")
    public ApiResponse<songResponse> disableSong(@PathVariable Long songId) {
        Song song = songService.disableSong(songId);
        songResponse response = songMapper.toDto(song);

        ApiResponse<songResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Song disabled successfully");
        apiResponse.setResult(response);
        return apiResponse;
    }
}
