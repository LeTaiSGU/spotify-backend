package com.spotify.spotify_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.album.AlbumRequestDTO;
import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.AlbumMapper;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.repository.AlbumRepository;
import com.spotify.spotify_backend.repository.ArtistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
        @Autowired
        private AlbumRepository albumRepository;

        @Autowired
        private ArtistRepository artistRepository;

        @Autowired
        private AlbumMapper albumMapper;

        private <T> PageResponseDTO<T> createPageDTO(Page<?> page, List<T> content) {
                return PageResponseDTO.<T>builder()
                                .content(content)
                                .pageNo(page.getNumber())
                                .pageSize(page.getSize())
                                .totalElements(page.getTotalElements())
                                .totalPages(page.getTotalPages())
                                .last(page.isLast())
                                .build();
        }

        // Phương thức mới hỗ trợ phân trang
        public PageResponseDTO<AlbumResponseDTO> getAllAlbumsPaginated(int pageNo, int pageSize,
                        String sortBy,
                        String sortDir) {
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
                Page<Album> albumPage = albumRepository.findAll(pageable);

                List<AlbumResponseDTO> content = albumPage.getContent().stream()
                                .map(albumMapper::toDTO)
                                .collect(Collectors.toList());

                PageResponseDTO<AlbumResponseDTO> pageResponseDTO = createPageDTO(albumPage, content);

                return pageResponseDTO;
        }

        // Lấy album theo id
        public AlbumResponseDTO getAlbumById(Long id) {
                Album album = albumRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                // Chuyển đổi Album thành AlbumResponseDTO
                AlbumResponseDTO albumResponseDTO = albumMapper.toDTO(album);
                return albumResponseDTO;
        }

        // Phương thức tìm kiếm có hỗ trợ phân trang
        public PageResponseDTO<AlbumResponseDTO> searchAlbumsByTitle(
                        String title, int pageNo, int pageSize, String sortBy, String sortDir) {

                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
                Page<Album> albumPage = albumRepository.findByTitleContainingIgnoreCase(title, pageable);

                List<AlbumResponseDTO> content = albumPage.getContent().stream()
                                .map(albumMapper::toDTO)
                                .collect(Collectors.toList());

                PageResponseDTO<AlbumResponseDTO> pageResponseDTO = createPageDTO(albumPage, content);

                return pageResponseDTO;
        }

        @Transactional
        public AlbumResponseDTO createAlbum(AlbumRequestDTO albumRequestDTO) {

                System.out.println(albumRequestDTO.getArtistId());
                Album album = albumMapper.toAlbum(albumRequestDTO, artistRepository);
                System.out.println(album.getArtist().getArtistId());
                Album savedAlbum = albumRepository.save(album);
                return albumMapper.toDTO(savedAlbum);
        }

        @Transactional
        public AlbumResponseDTO updateAlbum(Long id, AlbumRequestDTO albumRequestDTO) {
                Album album = albumRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

                albumMapper.updateAlbumFromDTO(albumRequestDTO, album, artistRepository);
                album.setUpdatedAt(LocalDateTime.now());

                Album updatedAlbum = albumRepository.save(album);
                // Chuyển đổi Album thành AlbumResponseDTO
                AlbumResponseDTO albumResponseDTO = albumMapper.toDTO(updatedAlbum);
                return albumResponseDTO;
        }

        @Transactional
        public Boolean deleteAlbum(Long id) {
                if (!albumRepository.existsById(id)) {
                        throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
                }
                albumRepository.updateStatusByAlbumId(id, false);
                return true;
        }

        // // Tìm kiếm album theo title
        // public List<AlbumResponseDTO> searchAlbumsByTitle(String title) {
        // List<Album> albums = albumRepository.findByTitleContainingIgnoreCase(title);
        // List<AlbumResponseDTO> responseDTOs = albums.stream()
        // .map(albumMapper::toDTO)
        // .collect(Collectors.toList());

        // return ApiResponse.<List<AlbumResponseDTO>>builder()
        // .code(1000)
        // .message("Tìm kiếm album thành công")
        // .result(responseDTOs)
        // .build();
        // }
}