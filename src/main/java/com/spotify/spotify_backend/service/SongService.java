package com.spotify.spotify_backend.service;

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

    public Song uploadSong(songdto songDto, MultipartFile songFile, MultipartFile imgFile) {
        try {
            Song song = songMapper.toSong(songDto, songMappingHelper);
            song.setCreatedAt(LocalDate.now());
            Song savedSong = songRepository.save(song);

            // Upload file nhạc
            String songUrl = awsS3Service.uploadFile("song_file", songFile, savedSong.getSongId());
            if (songUrl == null || songUrl.isEmpty()) {
                throw new AppException(ErrorCode.SONG_FILE_UPLOAD_FAILED, "Không thể upload file nhạc");
            }

            // Upload ảnh bài hát
            String imgUrl = awsS3Service.uploadFile("song_img", imgFile, savedSong.getSongId());
            if (imgUrl == null || imgUrl.isEmpty()) {
                throw new AppException(ErrorCode.SONG_IMG_UPLOAD_FAILED);
            }

            song.setFileUpload(songUrl);
            song.setImg(imgUrl);

            return songRepository.save(song);

        } catch (AppException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Lỗi nghiệp vụ: " + e.getMessage());
        } catch (Exception e) {
            throw new AppException(ErrorCode.SONG_CREATE_FAILED, "Lỗi không xác định: " + e.getMessage());
        }
    }

    // Disable song by ID
    public Song disableSong(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));
        song.setStatus(false);
        return songRepository.save(song);
    }
}
