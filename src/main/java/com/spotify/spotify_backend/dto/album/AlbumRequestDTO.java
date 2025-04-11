package com.spotify.spotify_backend.dto.album;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AlbumRequestDTO {

    @NotBlank(message = "Title không được để trống")
    private String title;

    @NotNull(message = "Ngày phát hành không được để trống")
    private LocalDate releaseDate;

    private String description;

    @NotBlank(message = "Cover image không được để trống")
    private String coverImage;

    @NotNull(message = "ID nghệ sĩ không được để trống")
    private Long artistId;

    @NotBlank(message = "Type không được để trống")
    @Pattern(regexp = "^(EP|Album)$", message = "Type chỉ được là 'EP' hoặc 'Album'")
    private String type;

    private Boolean status;
}
