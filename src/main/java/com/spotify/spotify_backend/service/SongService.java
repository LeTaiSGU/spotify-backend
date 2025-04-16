package com.spotify.spotify_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.song.songResponse;
import com.spotify.spotify_backend.dto.song.songUpdate;
import com.spotify.spotify_backend.dto.song.songdto;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.mapper.SongMapper;
import com.spotify.spotify_backend.mapper.SongMappingHelper;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.repository.SongRepository;
import com.spotify.spotify_backend.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.time.LocalDate;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongMapper songMapper;
    @Autowired
    private SongMappingHelper songMappingHelper;

    @Autowired
    private AmazonService awsS3Service;

    public List<Song> getAllSongs() {

        return songRepository.findAll();
    }

    public Page<songResponse> getAllSongPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("songId").ascending());
        Page<Song> songPage = songRepository.findAll(pageable);
        return songPage.map(songMapper::toDto);
    }

    // all by status
    public PageResponseDTO<songResponse> getAllSongsByStatusPaginated(int pageNo, int pageSize,
            String sortBy, String sortDir, Boolean status) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Song> songPage = songRepository.findAllByStatus(status, pageable);
        Page<songResponse> songResponsePage = songPage.map(songMapper::toDto);

        return PageResponseDTO.<songResponse>builder()
                .content(songResponsePage.getContent())
                .pageNo(songResponsePage.getNumber())
                .pageSize(songResponsePage.getSize())
                .totalElements(songResponsePage.getTotalElements())
                .totalPages(songResponsePage.getTotalPages())
                .last(songResponsePage.isLast())
                .build();
    }

    public Song uploadSong(songdto songDto, MultipartFile songFile, MultipartFile imgFile) {
        // Kiểm tra trùng tên bài hát
        if (songRepository.existsBySongName(songDto.getSongName())) {
            throw new IllegalArgumentException("Tên bài hát đã tồn tại!");
        }

        Song song = songMapper.toSong(songDto, songMappingHelper);
        song.setCreatedAt(LocalDate.now());

        Song savedSong = songRepository.save(song);

        // Upload songFile và imgFile song song
        CompletableFuture<String> songUrlFuture = CompletableFuture
                .supplyAsync(() -> awsS3Service.uploadFile("song_file", songFile, savedSong.getSongId()));

        CompletableFuture<String> imgUrlFuture = CompletableFuture
                .supplyAsync(() -> awsS3Service.uploadFile("song_img", imgFile, savedSong.getSongId()));

        try {
            // Đợi cả hai upload xong
            String songUrl = songUrlFuture.get();
            String imgUrl = imgUrlFuture.get();

            savedSong.setFileUpload(songUrl + "?t=" + System.currentTimeMillis());
            savedSong.setImg(imgUrl + "?t=" + System.currentTimeMillis());

            return songRepository.save(savedSong);
        } catch (InterruptedException | ExecutionException e) {
            // Ghi log lỗi nếu cần
            Thread.currentThread().interrupt(); // Khôi phục trạng thái interrupt
            throw new RuntimeException("Upload thất bại: " + e.getMessage(), e);
        }
    }

    // Disable song by ID
    public Song disableSong(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));
        song.setStatus(!song.getStatus());
        return songRepository.save(song);
    }

    // Edit Song
    // Edit Song
    public songResponse updateSong(Long songId, songUpdate songDto, MultipartFile newSongFile,
            MultipartFile newImgFile) {
        Song existingSong = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát với ID: " + songId));

        songMapper.updateSongFromDto(songDto, existingSong, songMappingHelper);

        if (newSongFile != null && !newSongFile.isEmpty()) {
            String newSongUrl = awsS3Service.uploadFile("song_file", newSongFile, songId);
            existingSong.setFileUpload(newSongUrl);
        }

        if (newImgFile != null && !newImgFile.isEmpty()) {
            String newImgUrl = awsS3Service.uploadFile("song_img", newImgFile, songId);
            existingSong.setImg(newImgUrl);
        }

        try {
            songRepository.save(existingSong);
            return songMapper.toDto(existingSong);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật bài hát: " + e.getMessage(), e);
        }
    }

}
