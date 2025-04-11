package com.spotify.spotify_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.album.AlbumRequestDTO;
import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.service.AlbumService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @GetMapping
    public ApiResponse<PageResponseDTO<AlbumResponseDTO>> getAllAlbumsPaginated(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        PageResponseDTO<AlbumResponseDTO> response = albumService.getAllAlbumsPaginated(pageNo, pageSize, sortBy,
                sortDir);
        // Tạo ApiResponse từ PageResponseDTO
        ApiResponse<PageResponseDTO<AlbumResponseDTO>> apiResponse = ApiResponse
                .<PageResponseDTO<AlbumResponseDTO>>builder()
                .code(1000)
                .message("Lấy danh sách album thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<AlbumResponseDTO> getAlbumById(@PathVariable Long id) {
        AlbumResponseDTO response = albumService.getAlbumById(id);

        // Tạo ApiResponse từ AlbumResponseDTO
        ApiResponse<AlbumResponseDTO> apiResponse = ApiResponse
                .<AlbumResponseDTO>builder()
                .code(1000)
                .message("Lấy album thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PostMapping("/create")
    public ApiResponse<AlbumResponseDTO> createAlbum(
            @Valid @RequestBody AlbumRequestDTO albumRequestDTO) {

        AlbumResponseDTO response = albumService.createAlbum(albumRequestDTO);
        // Tạo ApiResponse từ PageResponseDTO
        ApiResponse<AlbumResponseDTO> apiResponse = ApiResponse
                .<AlbumResponseDTO>builder()
                .code(1000)
                .message("Lấy danh sách album thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<AlbumResponseDTO> updateAlbum(
            @PathVariable Long id,
            @Valid @RequestBody AlbumRequestDTO albumRequestDTO) {

        AlbumResponseDTO response = albumService.updateAlbum(id, albumRequestDTO);
        // Tạo ApiResponse từ PageResponseDTO
        ApiResponse<AlbumResponseDTO> apiResponse = ApiResponse
                .<AlbumResponseDTO>builder()
                .code(1000)
                .message("Cập nhật album thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PutMapping("/delete/{id}")
    public ApiResponse<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        ApiResponse<Void> apiResponse = ApiResponse
                .<Void>builder()
                .code(1000)
                .message("Xóa album thành công")
                .result(null)
                .build();
        return apiResponse;
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDTO<AlbumResponseDTO>> searchAlbums(
            @RequestParam("title") String title,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        PageResponseDTO<AlbumResponseDTO> response = albumService.searchAlbumsByTitle(title, pageNo,
                pageSize, sortBy, sortDir);

        // Tạo ApiResponse từ PageResponseDTO
        ApiResponse<PageResponseDTO<AlbumResponseDTO>> apiResponse = ApiResponse
                .<PageResponseDTO<AlbumResponseDTO>>builder()
                .code(1000)
                .message("Lấy danh sách album thành công")
                .result(response)
                .build();
        return apiResponse;
    }
}