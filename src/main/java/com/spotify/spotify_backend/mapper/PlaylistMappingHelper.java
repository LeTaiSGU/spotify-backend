package com.spotify.spotify_backend.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.spotify.spotify_backend.model.Users; // Import đúng User của bạn
import com.spotify.spotify_backend.model.PlaylistSong;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.repository.SongRepository;
import com.spotify.spotify_backend.repository.UserRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaylistMappingHelper {

    private final UserRepository userRepository;
    private final SongRepository songRepository; // Giả sử bạn có một repository cho Song

    public Users mapUser(Long id) {
        if (id == null) {
            return null; // hoặc throw custom exception nếu bắt buộc phải có
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public List<PlaylistSong> mapSongIds(List<Long> songIds) {
        if (songIds == null || songIds.isEmpty()) {
            return Collections.emptyList(); // Trả về danh sách rỗng nếu không có songId
        }
        return songIds.stream()
                .map(songId -> {
                    PlaylistSong playlistSong = new PlaylistSong();
                    Song song = songRepository.findById(songId)
                            .orElseThrow(() -> new RuntimeException("Song not found with ID: " + songId)); // Kiểm tra
                                                                                                           // song tồn
                                                                                                           // tại
                    playlistSong.setSong(song); // Gán song vào playlist
                    return playlistSong;
                })
                .collect(Collectors.toList());
    }

}
