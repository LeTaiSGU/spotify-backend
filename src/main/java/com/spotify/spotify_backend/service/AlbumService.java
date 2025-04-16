package com.spotify.spotify_backend.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.album.AlbumRequestDTO;
import com.spotify.spotify_backend.dto.album.AlbumResponseDTO;
import com.spotify.spotify_backend.dto.album.AlbumUpdateDTO;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.AlbumMapper;
import com.spotify.spotify_backend.model.Album;
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

        @Autowired
        private AmazonService awsS3Service;

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

        // Phương thức lấy danh sách hỗ trợ phân trang và sắp xếp
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

        // Phương thức lấy danh sách album theo trạng thái
        public PageResponseDTO<AlbumResponseDTO> getAllAlbumsByStatusPaginated(int pageNo, int pageSize,
                        String sortBy, String sortDir,
                        Boolean status) {
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
                Page<Album> albumPage = albumRepository.findByStatus(pageable, status);

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
        public AlbumResponseDTO createAlbum(AlbumRequestDTO albumRequestDTO, MultipartFile coverImage) {
                // Tạo entity từ DTO và lưu tạm vào DB để lấy albumId
                Album album = albumMapper.toAlbum(albumRequestDTO, artistRepository);
                Album savedAlbum = albumRepository.save(album);

                // Nếu có ảnh thì mới upload
                if (coverImage != null && !coverImage.isEmpty()) {
                        try {
                                String coverImageUrl = awsS3Service.uploadFile("album_coverImage", coverImage,
                                                savedAlbum.getAlbumId());
                                savedAlbum.setCoverImage(coverImageUrl);
                                savedAlbum = albumRepository.save(savedAlbum);
                        } catch (Exception e) {
                                throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage(), e);
                        }
                }

                return albumMapper.toDTO(savedAlbum);
        }

        @Transactional
        public AlbumResponseDTO updateAlbum(AlbumUpdateDTO albumUpdateDTO, MultipartFile coverImage) {
                Album album = albumRepository.findById(albumUpdateDTO.getAlbumId())
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

                // Cập nhật thông tin album từ albumUpdateDTO
                album.setTitle(albumUpdateDTO.getTitle());
                album.setReleaseDate(albumUpdateDTO.getReleaseDate());
                album.setType(albumUpdateDTO.getType());

                // Nếu có ảnh mới
                if (coverImage != null && !coverImage.isEmpty()) {
                        // Nếu có ảnh cũ thì xóa khỏi AWS S3
                        String oldCoverImageUrl = album.getCoverImage();
                        if (oldCoverImageUrl != null && !oldCoverImageUrl.isBlank()) {
                                try {
                                        awsS3Service.deleteFile(oldCoverImageUrl);
                                } catch (Exception e) {
                                        // Log lỗi nếu muốn nhưng không throw để tránh chặn flow update
                                        System.err.println("Xoá ảnh cũ thất bại: " + e.getMessage());
                                }
                        }

                        // Upload ảnh mới
                        try {
                                String newCoverImageUrl = awsS3Service.uploadFile("album_coverImage", coverImage,
                                                album.getAlbumId());
                                album.setCoverImage(newCoverImageUrl);
                        } catch (Exception e) {
                                throw new RuntimeException("Upload ảnh mới thất bại: " + e.getMessage(), e);
                        }
                }

                // Lưu lại album
                Album updatedAlbum = albumRepository.save(album);
                return albumMapper.toDTO(updatedAlbum);
        }

        @Transactional
        public AlbumResponseDTO updateStatus(Long id) {
                Album album = albumRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                // Chuyển đổi trạng thái
                Boolean newStatus = !album.getStatus();
                album.setStatus(newStatus);
                albumRepository.save(album);
                return albumMapper.toDTO(album);
        }

        @Transactional
        public Boolean deleteAlbum(Long id) {
                if (!albumRepository.existsById(id)) {
                        throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
                }
                albumRepository.deleteById(id);
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