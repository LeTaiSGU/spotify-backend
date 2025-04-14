package com.spotify.spotify_backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.spotify.spotify_backend.config.JwtUtil;
import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.dto.users.GoogleAuthRequest;
import com.spotify.spotify_backend.dto.users.LoginRequest;
import com.spotify.spotify_backend.exception.AppException;
import com.spotify.spotify_backend.exception.ErrorCode;
import com.spotify.spotify_backend.model.Users;
import com.spotify.spotify_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Value("${google.clientId}")
    private String googleClientId;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    // Đăng ký người dùng mới
    public String signup(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByUserName(createUserDTO.getUserName())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        Users user = new Users();
        user.setUserName(createUserDTO.getUserName());
        user.setEmail(createUserDTO.getEmail());
        user.setPassHash(passwordEncoder.encode(createUserDTO.getPassword()));
        user.setDob(createUserDTO.getDob());
        user.setPremium(createUserDTO.isPremium());
        user.setRole("USER");
        user.setAuthProvider("LOCAL");

        userRepository.save(user);
        return jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());
    }

    // Đăng nhập thông thường
    public String login(LoginRequest loginRequest) {
        Users user = userRepository.findByEmailOrUserName(loginRequest.getIdentifier(), loginRequest.getIdentifier())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra mật khẩu (chỉ áp dụng cho LOCAL)
        if ("LOCAL".equals(user.getAuthProvider()) &&
                !passwordEncoder.matches(loginRequest.getPassword(), user.getPassHash())) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }

        // Tạo JWT
        return jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());
    }

    // Đăng nhập bằng Google
    public String googleLogin(GoogleAuthRequest googleAuthRequest) {
        try {
            GoogleIdToken idToken = googleIdTokenVerifier().verify(googleAuthRequest.getIdToken());
            if (idToken == null) {
                throw new AppException(ErrorCode.INVALID_GOOGLE_TOKEN);
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String username = email.split("@")[0];

            Users user = userRepository.findByAuthProviderAndEmail("GOOGLE", email)
                    .orElseGet(() -> {
                        String finalUsername = username;
                        int counter = 1;
                        while (userRepository.existsByUserName(finalUsername)) {
                            finalUsername = username + counter++;
                        }

                        Users newUser = new Users();
                        newUser.setEmail(email);
                        newUser.setUserName(finalUsername);
                        newUser.setRole("USER");
                        newUser.setAuthProvider("GOOGLE");
                        newUser.setPremium(false);
                        return userRepository.save(newUser);
                    });

            return jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());
        } catch (GeneralSecurityException | IOException e) {
            throw new AppException(ErrorCode.GOOGLE_LOGIN_FAILED);
        }
    }
}