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

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        System.out.println("Secret key initialized: " + secret); // Debug secret key
    }

    public String generateToken(Long userId, String email, String role) {
        Claims claims = Jwts.claims()
                .subject(email)
                .add("userId", userId)
                .add("role", role)
                .build();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
        System.out.println("Generated token with userId: " + userId + ", email: " + email + ", role: " + role);
        return token;
    }

    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", Long.class);
    }

    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    public boolean validateToken(String token, String email) {
        try {
            Claims claims = getClaimsFromToken(token);
            String extractedEmail = claims.getSubject();
            boolean isValid = extractedEmail.equals(email) && !claims.getExpiration().before(new Date());
            System.out.println("Token validation result for email " + email + ": " + isValid);
            return isValid;
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}