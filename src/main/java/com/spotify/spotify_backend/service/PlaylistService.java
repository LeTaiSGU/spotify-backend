package com.spotify.spotify_backend.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spotify.spotify_backend.dto.playlist.playlistDto;
import com.spotify.spotify_backend.dto.playlist.playlistUp;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.PlaylistMapper;
import com.spotify.spotify_backend.model.Playlist;
import com.spotify.spotify_backend.repository.PlaylistRepository;
import com.spotify.spotify_backend.repository.SongRepository;
import com.spotify.spotify_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PlaylistService {

    @Autowired
    private AmazonService awsS3Service;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private PlaylistSongService playlistSongService;

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Page<Playlist> getAllPlaylistsPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return playlistRepository.findAll(pageable);
    }

    public Page<Playlist> getAllPlaylistsPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return playlistRepository.findAll(pageable);
    }

    public Playlist createPlaylist(playlistDto newplaylist) {
        Playlist playlist = playlistMapper.toPlaylist(newplaylist, userRepository);
        return playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist createPlaylistAdmin(playlistDto dto, MultipartFile coverImage, List<Long> playlistSongIds) {
        Playlist playlist = playlistMapper.toPlaylist(dto, userRepository);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        // Upload ảnh đại diện
        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                String imgUrl = awsS3Service.uploadFile("playlist_img", coverImage, savedPlaylist.getPlaylistId());
                savedPlaylist.setCoverImage(imgUrl);
                savedPlaylist = playlistRepository.save(savedPlaylist); // Cập nhật lại với ảnh mới
            } catch (Exception e) {
                throw new RuntimeException("❌ Upload ảnh thất bại: " + e.getMessage(), e);
            }
        }
        // Lưu các bài hát vào playlist
        if (playlistSongIds != null) {
            for (Long songId : playlistSongIds) {
                playlistSongService.addSongToPlaylist(savedPlaylist.getPlaylistId(), songId);
            }
        }

        return savedPlaylist;

    }

    public List<Playlist> getPlaylistsByUserId(Long userId) {
        return playlistRepository.findByUser_UserIdAndStatusTrue(userId);
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    public Playlist editPlaylist(playlistUp playlistUp, MultipartFile avatar, Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_NOT_FOUND));

        playlistMapper.updatePlaylistFromDto(playlistUp, playlist);

        // Only upload avatar if it's not null
        if (avatar != null && !avatar.isEmpty()) {
            try {
                // Upload ảnh đại diện
                CompletableFuture<String> Avatar = CompletableFuture
                        .supplyAsync(() -> awsS3Service.uploadFile("playlist_img", avatar, id));

                String playlistAvatar = Avatar.get();
                // Thêm query string với timestamp để tránh cache
                playlist.setCoverImage(playlistAvatar + "?t=" + System.currentTimeMillis());
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                Throwable rootCause = e.getCause();
                if (rootCause != null) {
                    System.err.println("Lỗi gốc khi upload ảnh: " + rootCause.getMessage());
                }
                throw new RuntimeException("Lỗi upload ảnh playlist", e);
            }
        }

        return playlistRepository.save(playlist);
    }

    // Xóa playlist
    @Transactional
    public Playlist deletePlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy playlist với ID: " + playlistId));
        playlist.setStatus(false);
        // Xóa avatar từ S3 trước

        return playlist;
    }

    private String extractS3KeyFromUrl(String url) {
        return url.substring(url.indexOf("playlist_img/"));
    }

    //
    @Transactional
    public Playlist changePrivate(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy playlist với ID: " + playlistId));
        playlist.setIsPrivate(!playlist.getIsPrivate());
        return playlist;
    }

}
