package com.spotify.spotify_backend.service;

import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.artist.ArtistRequestDTO;
import com.spotify.spotify_backend.dto.artist.ArtistResponseDTO;
import com.spotify.spotify_backend.dto.artist.ArtistUpdateDTO;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.ArtistMapper;
import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.repository.ArtistRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class ArtistService {
        @Autowired
        private ArtistRepository artistRepository;

        @Autowired
        private ArtistMapper artistMapper;

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
        public PageResponseDTO<ArtistResponseDTO> getAllArtistsPaginated(int pageNo, int pageSize,
                        String sortBy,
                        String sortDir) {
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
                Page<Artist> artistPage = artistRepository.findAll(pageable);

                List<ArtistResponseDTO> content = artistPage.getContent().stream()
                                .map(artistMapper::toDTO)
                                .collect(Collectors.toList());

                PageResponseDTO<ArtistResponseDTO> pageResponseDTO = createPageDTO(artistPage, content);

                return pageResponseDTO;
        }

        // Phương thức lấy danh sách nghệ sĩ theo trạng thái
        public PageResponseDTO<ArtistResponseDTO> getAllArtistsByStatusPaginated(int pageNo, int pageSize,
                        String sortBy, String sortDir, Boolean status) {
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
                Page<Artist> artistPage = artistRepository.findByStatus(pageable, status);

                List<ArtistResponseDTO> content = artistPage.getContent().stream()
                                .map(artistMapper::toDTO)
                                .collect(Collectors.toList());

                PageResponseDTO<ArtistResponseDTO> pageResponseDTO = createPageDTO(artistPage, content);

                return pageResponseDTO;
        }

        // Phương thức lấy danh sách theo id nghệ sĩ
        public ArtistResponseDTO getArtistById(Long artistId) {
                Artist artist = artistRepository.findById(artistId)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                ArtistResponseDTO artistResponseDTO = artistMapper.toDTO(artist);
                return artistResponseDTO;
        }

        // Phương thức tìm kiếm có hỗ trợ phân trang
        public PageResponseDTO<ArtistResponseDTO> searchArtistsPaginated(int pageNo, int pageSize,
                        String sortBy,
                        String sortDir, String name) {
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
                Page<Artist> artistPage = artistRepository.findByNameContainingIgnoreCase(name, pageable);

                List<ArtistResponseDTO> content = artistPage.getContent().stream()
                                .map(artistMapper::toDTO)
                                .collect(Collectors.toList());

                PageResponseDTO<ArtistResponseDTO> pageResponseDTO = createPageDTO(artistPage, content);

                return pageResponseDTO;
        }

        // Phương thức tạo mới nghệ sĩ
        @Transactional
        public ArtistResponseDTO createArtist(ArtistRequestDTO artistRequestDTO, MultipartFile img) {
                // Tạo entity từ DTO và lưu vào DB
                Artist artist = artistMapper.toArtist(artistRequestDTO);
                Artist savedArtist = artistRepository.save(artist);

                // Nếu có ảnh mới, xử lý upload
                if (img != null && !img.isEmpty()) {
                        try {
                                String imgUrl = awsS3Service.uploadFile("artist_img", img, savedArtist.getArtistId());
                                savedArtist.setImg(imgUrl);
                                savedArtist = artistRepository.save(savedArtist); // Cập nhật lại với ảnh mới
                        } catch (Exception e) {
                                throw new RuntimeException("❌ Upload ảnh thất bại: " + e.getMessage(), e);
                        }
                }

                return artistMapper.toDTO(savedArtist);
        }

        // Phương thức cập nhật nghệ sĩ
        @Transactional
        public ArtistResponseDTO updateArtist(ArtistUpdateDTO artistUpdateDTO, MultipartFile newImg) {
                // Tìm nghệ sĩ từ ID
                Artist artist = artistRepository.findById(artistUpdateDTO.getArtistId())
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

                // Cập nhật thông tin khác của nghệ sĩ từ DTO
                artistMapper.updateArtistFromDTO(artistUpdateDTO, artist);

                // Nếu có ảnh mới, xử lý upload
                if (newImg != null && !newImg.isEmpty()) {
                        // Xóa ảnh cũ nếu có
                        String oldImgUrl = artist.getImg();
                        if (oldImgUrl != null && !oldImgUrl.isBlank()) {
                                try {
                                        awsS3Service.deleteFile(oldImgUrl);
                                } catch (Exception e) {
                                        // Log lỗi nhưng không throw, tránh gián đoạn quá trình cập nhật
                                        System.err.println("❌ Xoá ảnh cũ thất bại: " + e.getMessage());
                                }
                        }

                        // Upload ảnh mới
                        try {
                                String newImgUrl = awsS3Service.uploadFile("artist_img", newImg, artist.getArtistId());
                                artist.setImg(newImgUrl);
                        } catch (Exception e) {
                                throw new RuntimeException("❌ Upload ảnh mới thất bại: " + e.getMessage(), e);
                        }
                }

                // Lưu lại nghệ sĩ sau khi cập nhật
                Artist updatedArtist = artistRepository.save(artist);
                return artistMapper.toDTO(updatedArtist);
        }

        @Transactional
        public ArtistResponseDTO updateArtistStatus(Long artistId) {
                Artist artist = artistRepository.findById(artistId)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                // Chuyển đổi trạng thái
                artist.setStatus(!artist.getStatus());
                Artist updatedArtist = artistRepository.save(artist);
                return artistMapper.toDTO(updatedArtist);
        }

        @Transactional
        public ArtistResponseDTO deleteArtist(Long artistId) {
                Artist artist = artistRepository.findById(artistId)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                artistRepository.delete(artist);
                return artistMapper.toDTO(artist);
        }
}
