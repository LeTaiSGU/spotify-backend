package com.spotify.spotify_backend.service;

import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.model.Song;
import com.spotify.spotify_backend.repository.SongRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AmazonService awsS3Service;

    public Song uploadSong(Song song, MultipartFile songFile, MultipartFile imgFile) {
        String songUrl = awsS3Service.uploadFile("song_file", songFile, song.getSongId());
        String imgUrl = awsS3Service.uploadFile("song_img", imgFile, song.getSongId());

//        Song song = Song.builder()
//                .songName(songName)
//                .fileUpload(songUrl)
//                .img(imgUrl)
//                .createdAt(LocalDateTime.now())
//                .build();

        return songRepository.save(song);
    }

    public Song createSong(Song song){
        return songRepository.save(song);
    }

}
