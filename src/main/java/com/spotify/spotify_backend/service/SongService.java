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
import java.util.stream.Collectors;
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

    private <T> PageResponseDTO<T> createPageDTO(Page<?> page, List<T> content) {
        return PageResponseDTO.<T>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public List<Song> getAllSongs() {

        return songRepository.findAll();
    }

    public PageResponseDTO<songResponse> getAllSongPage(int pageNo, int pageSize,
            String sortBy,
            String sortDir, Boolean status) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Song> songPage;
        if (status != null) {
            songPage = songRepository.findAllByStatus(status, pageable);
        } else {
            songPage = songRepository.findAll(pageable);
        }
        List<songResponse> content = songPage.getContent().stream()
                .map(songMapper::toDto)
                .collect(Collectors.toList());

        PageResponseDTO<songResponse> pageResponseDTO = createPageDTO(songPage, content);
        return pageResponseDTO;
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

    // Get song by ID
    public songResponse getSongById(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));

        return songMapper.toDto(song);
    }

    public Song uploadSong(songdto songDto, MultipartFile songFile, MultipartFile imgFile) {
        // Kiểm tra trùng tên bài hát
        // if (songRepository.existsBySongName(songDto.getSongName())) {
        // throw new IllegalArgumentException("Tên bài hát đã tồn tại!");
        // }

        Song song = songMapper.toSong(songDto, songMappingHelper);
        song.setCreatedAt(LocalDate.now());
        System.out.println(song);
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
            // add time State
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
    public songResponse updateSong(songUpdate songDto, MultipartFile newSongFile,
            MultipartFile newImgFile) {
        Song existingSong = songRepository.findById(songDto.getSongId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát với ID: " + songDto.getSongId()));

        songMapper.updateSongFromDto(songDto, existingSong, songMappingHelper);

        if (newSongFile != null && !newSongFile.isEmpty()) {
            String newSongUrl = awsS3Service.uploadFile("song_file", newSongFile, songDto.getSongId());
            existingSong.setFileUpload(newSongUrl);
        }

        if (newImgFile != null && !newImgFile.isEmpty()) {
            String newImgUrl = awsS3Service.uploadFile("song_img", newImgFile, songDto.getSongId());
            existingSong.setImg(newImgUrl);
        }

        try {
            songRepository.save(existingSong);
            return songMapper.toDto(existingSong);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật bài hát: " + e.getMessage(), e);
        }
    }

    // random song
    public Song getRandomSong(Long excludeId) {
        if (excludeId != null) {
            return songRepository.findRandomSongExclude(excludeId);
        } else {
            return songRepository.findRandomSong();
        }
    }

}
