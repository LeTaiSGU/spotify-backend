package com.spotify.spotify_backend.repository;

import com.spotify.spotify_backend.model.Album;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Album tồn tại hay không
    boolean existsByTitle(String title);

    // Phương thức mới hỗ trợ phân trang
    Page<Album> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Album a SET a.status = :status WHERE a.id = :albumId")
    int updateStatusByAlbumId(@Param("albumId") Long albumId, @Param("status") Boolean status);

}