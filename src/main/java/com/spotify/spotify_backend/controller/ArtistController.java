package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.artist.artistdto;
import com.spotify.spotify_backend.mapper.ArtistMapper;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.service.AmazonService;
import com.spotify.spotify_backend.service.ArtistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AmazonService awsS3Service;

    @Autowired
    private ArtistMapper artistMapper;

    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    @PostMapping("/artist")
    public ApiResponse<Artist> createArtist(@RequestPart("artist") artistdto artistDTO,
                                            @RequestPart("imgFile") MultipartFile imgFile,
                                            HttpServletRequest request) {
        System.out.println("Content-Type: " + request.getContentType());
        String imgName = imgFile.getOriginalFilename();
        System.out.println("Image Name: " + imgName);
//        artistDTO.setImg(imgName);
        ApiResponse<Artist> apiResponse = new ApiResponse<>();
        boolean uploadSuccess = awsS3Service.uploadArtistImage(imgFile, artistDTO.getName());
        if (!uploadSuccess) {
            apiResponse.setCode(9999);
            apiResponse.setMessage("Image upload failed");
            apiResponse.setResult(null);
            return apiResponse;
        }
        apiResponse.setResult(artistService.createArtist(artistDTO,imgName));
        apiResponse.setCode(1000);
        return apiResponse;
    }
}
