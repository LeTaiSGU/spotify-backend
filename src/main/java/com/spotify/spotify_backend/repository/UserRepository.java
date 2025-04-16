package com.spotify.spotify_backend.repository;

import com.spotify.spotify_backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email); // Thêm để tìm người dùng khi đăng nhập

    Optional<Users> findByAuthProviderAndEmail(String authProvider, String email); // Thêm để tìm người dùng khi đăng
                                                                                   // nhập Google

    Optional<Users> findByEmailOrUserName(String email, String userName);
}