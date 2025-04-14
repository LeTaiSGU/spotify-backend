package com.spotify.spotify_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artists")
@EntityListeners(AuditingEntityListener.class) // Kích hoạt Auditing
public class Artist {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long artistId;

        private String name;
        private String img;
        private String description;

        @CreatedDate
        @Column(updatable = false) // Không cho phép cập nhật lại
        private LocalDate createdAt;

        // @LastModifiedDate
        // private LocalDateTime updatedAt;

        // @OneToMany(mappedBy = "artist")
        // private Set<Song> songs; // Danh sách bài hát của nghệ sĩ

        @ManyToMany(mappedBy = "featuredArtists")
        private Set<Song> featuredSongs;

        Boolean status; // Trạng thái nghệ sĩ (có thể là true hoặc false)
}
