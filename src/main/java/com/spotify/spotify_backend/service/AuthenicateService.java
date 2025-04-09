//package com.spotify.spotify_backend.service;
//
//import com.spotify.spotify_backend.dto.users.loginRequest;
//import com.spotify.spotify_backend.exception.AppException;
//import com.spotify.spotify_backend.exception.ErrorCode;
//import com.spotify.spotify_backend.repository.UserRepository;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Data
//@RequiredArgsConstructor
//@Service
////@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
//public class AuthenicateService {
//    @Autowired
//    UserRepository userRepository;
//
//    public boolean authenicatUser(loginRequest loginRequest) {
//        var user = userRepository.findByUserName(loginRequest.getUsername())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        return passwordEncoder.matches(loginRequest.getPassword(), user.getPassHash());
//    }
//}
