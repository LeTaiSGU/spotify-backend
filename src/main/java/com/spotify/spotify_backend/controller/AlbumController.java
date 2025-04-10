package com.spotify.spotify_backend.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.album.AlbumDTO;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.service.AlbumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @GetMapping("/")
    public ApiResponse<List<Album>> getAllAlbums() {
        ApiResponse<List<Album>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Success");
        apiResponse.setResult(albumService.getAllAlbums());
        return apiResponse;
    }
}
