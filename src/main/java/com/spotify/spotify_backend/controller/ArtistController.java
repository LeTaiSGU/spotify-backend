package com.spotify.spotify_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.artist.ArtistRequestDTO;
import com.spotify.spotify_backend.dto.artist.ArtistResponseDTO;
import com.spotify.spotify_backend.dto.artist.ArtistUpdateDTO;
import com.spotify.spotify_backend.service.ArtistService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/all")
    public ApiResponse<PageResponseDTO<ArtistResponseDTO>> getAllArtistsPaginated(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageResponseDTO<ArtistResponseDTO> response = artistService.getAllArtistsPaginated(pageNo, pageSize, sortBy,
                sortDir);
        // Tạo ApiResponse từ PageResponseDTO
        ApiResponse<PageResponseDTO<ArtistResponseDTO>> apiResponse = ApiResponse
                .<PageResponseDTO<ArtistResponseDTO>>builder()
                .code(1000)
                .message("Lấy danh sách nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @GetMapping("/allstatus")
    public ApiResponse<PageResponseDTO<ArtistResponseDTO>> getAllArtistsByStatusPaginated(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "status", defaultValue = "true", required = false) Boolean status) {
        PageResponseDTO<ArtistResponseDTO> response = artistService.getAllArtistsByStatusPaginated(pageNo, pageSize,
                sortBy, sortDir, status);
        // Tạo ApiResponse từ PageResponseDTO
        ApiResponse<PageResponseDTO<ArtistResponseDTO>> apiResponse = ApiResponse
                .<PageResponseDTO<ArtistResponseDTO>>builder()
                .code(1000)
                .message("Lấy danh sách nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<ArtistResponseDTO> getArtistById(@RequestParam(value = "id") Long id) {
        ArtistResponseDTO response = artistService.getArtistById(id);
        // Tạo ApiResponse từ ArtistResponseDTO
        ApiResponse<ArtistResponseDTO> apiResponse = ApiResponse
                .<ArtistResponseDTO>builder()
                .code(1000)
                .message("Lấy thông tin nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PostMapping("/create")
    public ApiResponse<ArtistResponseDTO> createArtist(@RequestBody ArtistRequestDTO artistRequestDTO) {
        ArtistResponseDTO response = artistService.createArtist(artistRequestDTO);
        // Tạo ApiResponse từ ArtistResponseDTO
        ApiResponse<ArtistResponseDTO> apiResponse = ApiResponse
                .<ArtistResponseDTO>builder()
                .code(1000)
                .message("Tạo nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PutMapping("/update")
    public ApiResponse<ArtistResponseDTO> updateArtist(@Valid @RequestBody ArtistUpdateDTO artistUpdateDTO) {
        ArtistResponseDTO response = artistService.updateArtist(artistUpdateDTO);
        // Tạo ApiResponse từ ArtistResponseDTO
        ApiResponse<ArtistResponseDTO> apiResponse = ApiResponse
                .<ArtistResponseDTO>builder()
                .code(1000)
                .message("Cập nhật nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PutMapping("/cancel/{id}")
    public ApiResponse<ArtistResponseDTO> cancelArtist(@PathVariable Long id) {
        ArtistResponseDTO response = artistService.cancelArtist(id);
        // Tạo ApiResponse từ ArtistResponseDTO
        ApiResponse<ArtistResponseDTO> apiResponse = ApiResponse
                .<ArtistResponseDTO>builder()
                .code(1000)
                .message("Hủy nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PutMapping("/restore/{id}")
    public ApiResponse<ArtistResponseDTO> restoreArtist(@PathVariable Long id) {
        ArtistResponseDTO response = artistService.restoreArtist(id);
        // Tạo ApiResponse từ ArtistResponseDTO
        ApiResponse<ArtistResponseDTO> apiResponse = ApiResponse
                .<ArtistResponseDTO>builder()
                .code(1000)
                .message("Khôi phục nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }

    @PutMapping("/delete/{id}")
    public ApiResponse<ArtistResponseDTO> deleteArtist(@PathVariable Long id) {
        ArtistResponseDTO response = artistService.deleteArtist(id);
        // Tạo ApiResponse từ ArtistResponseDTO
        ApiResponse<ArtistResponseDTO> apiResponse = ApiResponse
                .<ArtistResponseDTO>builder()
                .code(1000)
                .message("Xóa nghệ sĩ thành công")
                .result(response)
                .build();
        return apiResponse;
    }
}
