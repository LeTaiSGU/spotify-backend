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
import com.spotify.spotify_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PlaylistService {

    @Autowired
    private AmazonService awsS3Service;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistMapper playlistMapper;

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

    public List<Playlist> getPlaylistsByUserId(Long userId) {
        return playlistRepository.findByUser_UserId(userId);
    }

    public Playlist editPlaylist(playlistUp playlistUp, MultipartFile avatar, Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));
        playlistMapper.updatePlaylistFromDto(playlistUp, playlist);
        try {
            // Upload ảnh đại diện
            CompletableFuture<String> Avatar = CompletableFuture
                    .supplyAsync(() -> awsS3Service.uploadFile("playlist_img", avatar, id));

            String playlistAvatar = Avatar.get(); // Có thể ném InterruptedException hoặc ExecutionException
            playlist.setCoverImage(playlistAvatar);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt(); // Khôi phục trạng thái interrupt nếu bị Interrupted
            throw new RuntimeException("Lỗi upload ảnh playlist", e);
        }
        return playlistRepository.save(playlist);
    }

    // Xóa playlist
    @Transactional
    public String deletePlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy playlist với ID: " + playlistId));

        // Xóa avatar từ S3 trước
        if (!"/src/assets/default.png".equals(playlist.getCoverImage())) {
            String key = extractS3KeyFromUrl(playlist.getCoverImage());
            awsS3Service.deleteFile(key);
        }

        String deletedName = playlist.getName();
        playlistRepository.delete(playlist);
        return deletedName;
    }

    private String extractS3KeyFromUrl(String url) {
        return url.substring(url.indexOf("playlist_img/"));
    }

}
