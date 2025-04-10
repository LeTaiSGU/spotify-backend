package com.spotify.spotify_backend.dto.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class songdto {
    String songName;
    Long artist_id;
    Long album_id;
    Long duration;
    String description;
    boolean status = true;
    Set<Long> featuredArtistIds;
}
