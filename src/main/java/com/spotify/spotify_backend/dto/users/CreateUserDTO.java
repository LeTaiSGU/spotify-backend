package com.spotify.spotify_backend.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDTO {
    @NotBlank(message = "Tên người dùng bắt buộc")
    private String userName;

    @NotBlank(message = "Họ tên bắt buộc")
    private String fullname;

    @NotBlank(message = "Mật khẩu bắt buộc")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Email bắt buộc")
    @Email(message = "Email không hợp lệ")
    private String email;

    private LocalDate dob;

    private boolean isPremium = false;

    private Boolean status = true; // true: active, false: inactive
}