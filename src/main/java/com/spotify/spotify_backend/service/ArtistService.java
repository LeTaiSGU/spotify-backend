package com.spotify.spotify_backend.service;

import com.spotify.spotify_backend.dto.artist.artistdto;
import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.ArtistMapper;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Users;
import com.spotify.spotify_backend.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistMapper artistMapper;

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist createArtist(artistdto request,String img) {
        if (artistRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ARTIST_EXISTED);
        }
        Artist newArtist = artistMapper.toArtist(request);
        newArtist.setImg(img);
        return artistRepository.save(newArtist);
    }

}
