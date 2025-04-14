package com.spotify.spotify_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.album.AlbumRequestDTO;
import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.dto.album.AlbumUpdateDTO;
import com.spotify.spotify_backend.service.AlbumService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/album")
public class AlbumController {
        @Autowired
        private AlbumService albumService;

        @GetMapping("/all")
        public ApiResponse<PageResponseDTO<AlbumResponseDTO>> getAllAlbumsPaginated(
                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                        @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

                PageResponseDTO<AlbumResponseDTO> response = albumService.getAllAlbumsPaginated(pageNo, pageSize,
                                sortBy,
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

        @GetMapping("/allstatus")
        public ApiResponse<PageResponseDTO<AlbumResponseDTO>> getAllAlbumsByStatusPaginated(
                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                        @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                        @RequestParam(value = "status", defaultValue = "true", required = false) Boolean status) {

                PageResponseDTO<AlbumResponseDTO> response = albumService.getAllAlbumsByStatusPaginated(pageNo,
                                pageSize,
                                sortBy,
                                sortDir,
                                status);
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
                        @Valid @RequestPart AlbumRequestDTO albumRequestDTO,
                        @RequestPart("coverImage") MultipartFile coverImage) {

                AlbumResponseDTO response = albumService.createAlbum(albumRequestDTO, coverImage);
                // Tạo ApiResponse từ PageResponseDTO
                ApiResponse<AlbumResponseDTO> apiResponse = ApiResponse
                                .<AlbumResponseDTO>builder()
                                .code(1000)
                                .message("Lấy danh sách album thành công")
                                .result(response)
                                .build();
                return apiResponse;
        }

        @PutMapping("/update")
        public ApiResponse<AlbumResponseDTO> updateAlbum(
                        @Valid @RequestBody AlbumUpdateDTO albumUpdateDTO) {

                AlbumResponseDTO response = albumService.updateAlbum(albumUpdateDTO);
                // Tạo ApiResponse từ PageResponseDTO
                ApiResponse<AlbumResponseDTO> apiResponse = ApiResponse
                                .<AlbumResponseDTO>builder()
                                .code(1000)
                                .message("Cập nhật album thành công")
                                .result(response)
                                .build();
                return apiResponse;
        }

        @PutMapping("/cancel/{id}")
        public ApiResponse<Void> cancelAlbum(@PathVariable Long id) {
                albumService.cancelAlbum(id);
                ApiResponse<Void> apiResponse = ApiResponse
                                .<Void>builder()
                                .code(1000)
                                .message("Hủy album thành công")
                                .result(null)
                                .build();
                return apiResponse;
        }

        @PutMapping("/restore/{id}")
        public ApiResponse<Void> restoreAlbum(@PathVariable Long id) {
                albumService.restoreAlbum(id);
                ApiResponse<Void> apiResponse = ApiResponse
                                .<Void>builder()
                                .code(1000)
                                .message("Khôi phục album thành công")
                                .result(null)
                                .build();
                return apiResponse;
        }

        @DeleteMapping("/delete/{id}")
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