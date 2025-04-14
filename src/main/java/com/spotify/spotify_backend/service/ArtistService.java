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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistService {
        @Autowired
        private ArtistRepository artistRepository;

        @Autowired
        private ArtistMapper artistMapper;

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
        public ArtistResponseDTO createArtist(ArtistRequestDTO artistRequestDTO) {

                Artist artist = artistMapper.toArtist(artistRequestDTO);
                Artist savedArtist = artistRepository.save(artist);
                ArtistResponseDTO artistResponseDTO = artistMapper.toDTO(savedArtist);
                return artistResponseDTO;
        }

        // Phương thức cập nhật nghệ sĩ
        @Transactional
        public ArtistResponseDTO updateArtist(ArtistUpdateDTO artistUpdateDTO) {
                Artist artist = artistRepository.findById(artistUpdateDTO.getArtistId())
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                artistMapper.updateArtistFromDTO(artistUpdateDTO, artist);
                Artist updatedArtist = artistRepository.save(artist);
                ArtistResponseDTO artistResponseDTO = artistMapper.toDTO(updatedArtist);
                return artistResponseDTO;
        }

        // Phương thức hủy nghệ sĩ
        @Transactional
        public ArtistResponseDTO cancelArtist(Long artistId) {
                Artist artist = artistRepository.findById(artistId)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                artist.setStatus(false);
                Artist updatedArtist = artistRepository.save(artist);
                ArtistResponseDTO artistResponseDTO = artistMapper.toDTO(updatedArtist);
                return artistResponseDTO;
        }

        // Phương thức khôi phục nghệ sĩ
        @Transactional
        public ArtistResponseDTO restoreArtist(Long artistId) {
                Artist artist = artistRepository.findById(artistId)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                artist.setStatus(true);
                Artist updatedArtist = artistRepository.save(artist);
                ArtistResponseDTO artistResponseDTO = artistMapper.toDTO(updatedArtist);
                return artistResponseDTO;
        }

        @Transactional
        public ArtistResponseDTO deleteArtist(Long artistId) {
                Artist artist = artistRepository.findById(artistId)
                                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                artistRepository.delete(artist);
                return artistMapper.toDTO(artist);
        }
}
