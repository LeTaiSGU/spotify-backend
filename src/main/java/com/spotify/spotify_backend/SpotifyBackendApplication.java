package com.spotify.spotify_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Kích hoạt tự động cập nhật thời gian
public class SpotifyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotifyBackendApplication.class, args);
	}

}
