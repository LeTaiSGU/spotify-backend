package com.spotify.spotify_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify.spotify_backend.dto.album.AlbumDTO;
import com.spotify.spotify_backend.mapper.AlbumMapper;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.repository.AlbumRepository;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        return albums;
    }
}
