package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.dto.users.GoogleAuthRequest;
import com.spotify.spotify_backend.dto.users.LoginRequest;
import com.spotify.spotify_backend.service.AuthenticateService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateController {

        private final AuthenticateService authenticateService;

        @PostMapping("/signup")
        public ResponseEntity<String> signup(@Valid @RequestBody CreateUserDTO createUserDTO,
                        HttpServletResponse response) {
                String token = authenticateService.signup(createUserDTO);
                addJwtToCookie(token, response);
                return ResponseEntity.ok("Signup successful");
        }

        @PostMapping("/login")
        public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest,
                        HttpServletResponse response) {
                String token = authenticateService.login(loginRequest);
                addJwtToCookie(token, response);
                return ResponseEntity.ok("Login successful");
        }

        @PostMapping("/google-login")
        public ResponseEntity<String> googleLogin(@Valid @RequestBody GoogleAuthRequest googleAuthRequest,
                        HttpServletResponse response) {
                String token = authenticateService.googleLogin(googleAuthRequest);
                addJwtToCookie(token, response);
                return ResponseEntity.ok("Google login successful");
        }

        private void addJwtToCookie(String token, HttpServletResponse response) {
                Cookie cookie = new Cookie("jwt", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(false); // Đặt false cho local (HTTP), true cho production (HTTPS)
                cookie.setPath("/");
                cookie.setMaxAge(24 * 60 * 60); // 1 ngày
                cookie.setAttribute("SameSite", "Strict");
                response.addCookie(cookie);
        }
}