package com.spotify.spotify_backend.dto.song;

import com.spotify.spotify_backend.model.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class songdto {
    String songname;
    long artist_id;
    long album_id;
    LocalDateTime duration;
    String img;
    String fileUpload;
    String description;
    LocalDateTime createdAt;
}
