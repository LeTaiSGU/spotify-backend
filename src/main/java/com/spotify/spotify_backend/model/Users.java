package com.spotify.spotify_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "pass_hash")
    private String passHash;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "is_premium")
    private boolean isPremium;

    @Column(name = "role", nullable = false)
    private String role; // "USER" hoặc "ADMIN"

    @Column(name = "auth_provider", nullable = false)
    private String authProvider; // "LOCAL" hoặc "GOOGLE"

    @Column(name = "avatar")
    private String avatar;
}
