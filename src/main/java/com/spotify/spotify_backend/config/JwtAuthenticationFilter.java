package com.spotify.spotify_backend.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader); // Debug header

        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Extracted Token: " + token); // Debug token
            try {
                email = jwtUtil.getEmailFromToken(token);
                System.out.println("Email from token: " + email);
            } catch (ExpiredJwtException e) {
                System.out.println("Token đã hết hạn: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token đã hết hạn");
                return;
            } catch (MalformedJwtException | SignatureException e) {
                System.out.println("Token không hợp lệ: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token không hợp lệ");
                return;
            } catch (Exception e) {
                System.out.println("Lỗi khác khi giải mã token: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Lỗi khi giải mã token");
                return;
            }
        } else {
            System.out.println("Không tìm thấy header Authorization hoặc token không hợp lệ: " + authHeader);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtUtil.getRoleFromToken(token);
            System.out.println("Role from token: " + role);
            if (jwtUtil.validateToken(token, email)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set for user: " + email + " with role: ROLE_" + role);
            } else {
                System.out.println("Token validation failed for email: " + email);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token validation failed");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
