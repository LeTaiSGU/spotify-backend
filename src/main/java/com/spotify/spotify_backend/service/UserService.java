package com.spotify.spotify_backend.service;

import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.dto.users.UserResponseDTO;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.UserMapper;
import com.spotify.spotify_backend.model.Album;
import com.spotify.spotify_backend.model.Users;
import com.spotify.spotify_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

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

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public PageResponseDTO<UserResponseDTO> getAllUsersPaginated(
            int pageNo, int pageSize,
            String sortBy, String sortDir,
            Boolean status) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Users> userPage;
        if (status == null) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.findAllByStatus(pageable, status);
        }

        List<UserResponseDTO> content = userPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        PageResponseDTO<UserResponseDTO> pageResponseDTO = createPageDTO(userPage, content);

        return pageResponseDTO;
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Users createUser(CreateUserDTO request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Users newUser = userMapper.toUsers(request);
        newUser.setAuthProvider("LOCAL"); // Gán giá trị mặc định
        newUser.setRole("USER"); // Gán giá trị mặc định
        return userRepository.save(newUser);
    }

}