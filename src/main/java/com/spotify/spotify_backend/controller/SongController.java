package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.dto.song.songUpdate;
import com.spotify.spotify_backend.dto.song.songdto;
import com.spotify.spotify_backend.mapper.SongMapper;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
                List<songResponse> songList = songService.getAllSongs().stream()
                                .map(song -> songMapper.toDto(song))
                                .toList();
                // List<Song> songList = songService.getAllSongs();
                ApiResponse<List<songResponse>> apiResponse = ApiResponse.<List<songResponse>>builder()
                                .code(1000)
                                .result(songList)
                                .message("List all of Song")
                                .build();
                return apiResponse;
        }

        @GetMapping("/page")
        public ApiResponse<PageResponseDTO<songResponse>> getAllSongsByStatusPaginated(
                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                        @RequestParam(value = "sortBy", defaultValue = "songName", required = false) String sortBy,
                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                        @RequestParam(value = "status", defaultValue = "true", required = false) Boolean status) {

                PageResponseDTO<songResponse> response = songService.getAllSongsByStatusPaginated(pageNo, pageSize,
                                sortBy,
                                sortDir, status);

                ApiResponse<PageResponseDTO<songResponse>> apiResponse = ApiResponse
                                .<PageResponseDTO<songResponse>>builder()
                                .code(1000)
                                .message("Lấy danh sách bài hát thành công")
                                .result(response)
                                .build();

                return apiResponse;
        }

        // Pagging
        @GetMapping("/pageAdmin")
        public ApiResponse<PageResponseDTO<songResponse>> getAllSongsPaged(
                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                        @RequestParam(value = "sortBy", defaultValue = "songId", required = false) String sortBy,
                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                        @RequestParam(value = "status", required = false) Boolean status) {

                PageResponseDTO<songResponse> response = songService.getAllSongPage(pageNo, pageSize,
                                sortBy,
                                sortDir, status);

                ApiResponse<PageResponseDTO<songResponse>> apiResponse = ApiResponse
                                .<PageResponseDTO<songResponse>>builder()
                                .code(1000)
                                .message("Lấy danh sách song thành công")
                                .result(response)
                                .build();
                return apiResponse;
        }

        @GetMapping("/{songId}")
        public ApiResponse<songResponse> getSongById(@PathVariable Long songId) {
                songResponse response = songService.getSongById(songId);
                ApiResponse<songResponse> apiResponse = new ApiResponse<>();
                apiResponse.setResult(response);
                return apiResponse;
        }

        @GetMapping("/random")
        public ApiResponse<songResponse> getRandomSong(@RequestParam(required = false) Long exclude) {
                try {
                        Song song = songService.getRandomSong(exclude);
                        songResponse response = songMapper.toDto(song);

                        return ApiResponse.<songResponse>builder()
                                        .code(1000)
                                        .message("Lấy bài hát ngẫu nhiên thành công")
                                        .result(response)
                                        .build();
                } catch (Exception e) {
                        return ApiResponse.<songResponse>builder()
                                        .code(500)
                                        .message("Lỗi khi lấy bài hát ngẫu nhiên")
                                        .result(null)
                                        .build();
                }
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

        @PutMapping(value = "/update")
        public ApiResponse<songResponse> editSong(
                        @RequestPart("song") songUpdate song,
                        @RequestPart(value = "songFile", required = false) MultipartFile songFile,
                        @RequestPart(value = "imgFile", required = false) MultipartFile imgFile) {

                ApiResponse<songResponse> apiResponse = new ApiResponse<>();
                songResponse updatedSong = songService.updateSong(song, songFile,
                                imgFile);
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
