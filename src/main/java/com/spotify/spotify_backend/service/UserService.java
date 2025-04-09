package com.spotify.spotify_backend.service;

import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.mapper.UserMapper;
import com.spotify.spotify_backend.model.Users;
import com.spotify.spotify_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

//    public Users createUser(CreateUserDTO request) {
//        if (userRepository.existsByUserName(request.getUserName())) {
//            throw new AppException(ErrorCode.USER_EXISTED);
//        }
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new AppException(ErrorCode.EMAIL_EXISTED);
//        }
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        request.setPassHash(passwordEncoder.encode(request.getPassHash()));
//        Users newUser = userMapper.toUsers(request);
//        return userRepository.save(newUser);
//    }


}
