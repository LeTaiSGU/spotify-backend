package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    @Autowired
    private SongService songService;

    @PostMapping("/upload")
    public ApiResponse<Song> uploadSong(
            @RequestPart("song") Song song,
            @RequestPart("songFile") MultipartFile songFile,
            @RequestPart("imgFile") MultipartFile imgFile) {

        songService.createSong(song);
        ApiResponse<Song> apiResponse = new ApiResponse<>();
        apiResponse.setResult(songService.uploadSong(song, songFile, imgFile));
        return apiResponse;
    }

}
