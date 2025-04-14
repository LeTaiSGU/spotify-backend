package com.spotify.spotify_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key;

    // Constructor để khởi tạo SecretKey từ secret
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes()); // Tạo SecretKey từ secret
    }

    // Tạo JWT với userId, email, và role
    public String generateToken(Long userId, String email, String role) {
        // Tạo ClaimsBuilder và thiết lập các thuộc tính
        Claims claims = Jwts.claims()
                .subject(email) // Dùng email làm subject
                .add("userId", userId) // Thêm userId vào claims
                .add("role", role) // Thêm role vào claims
                .build(); // Build để lấy Claims

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, Jwts.SIG.HS512) // Sử dụng SecretKey và thuật toán HS512
                .compact();
    }

    // Lấy email từ token
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Lấy userId từ token
    public Long getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", Long.class);
    }

    // Lấy role từ token
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    // Xác thực token
    public boolean validateToken(String token, String email) {
        try {
            Claims claims = getClaimsFromToken(token);
            String extractedEmail = claims.getSubject();
            return extractedEmail.equals(email) && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key) // Sử dụng SecretKey để xác thực
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}