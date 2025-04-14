package com.spotify.spotify_backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ErrorCode {
    // Lỗi chung
    UNCATEGORIZED(9999, "Lỗi không xác định"),
    VALIDATION_ERROR(4000, "Dữ liệu không hợp lệ"),
    RESOURCE_NOT_FOUND(4004, "Không tìm thấy tài nguyên"),

    // Lỗi liên quan đến user
    USER_NOT_FOUND(5000, "Người dùng không tồn tại"),
    EMAIL_ALREADY_EXISTS(5001, "Email đã tồn tại"),
    USERNAME_ALREADY_EXISTS(5002, "Tên người dùng đã tồn tại"),
    PASSWORD_INVALID(5003, "Mật khẩu không hợp lệ"),
    IDENTIFIER_REQUIRED(5004, "Email hoặc tên người dùng bắt buộc"),

    // Lỗi liên quan đến Google OAuth
    INVALID_GOOGLE_TOKEN(5100, "Google token không hợp lệ"),
    GOOGLE_LOGIN_FAILED(5101, "Đăng nhập Google thất bại"),

    // Lỗi liên quan đến bài hát
    SONG_CREATE_FAILED(5200, "Không tạo được bài hát"),
    SONG_NOT_FOUND(5201, "Bài hát không tồn tại"),
    SONG_FILE_UPLOAD_FAILED(5202, "Không upload được file bài hát"),
    SONG_IMAGE_UPLOAD_FAILED(5203, "Không upload được hình bài hát"),

    // Lỗi liên quan đến nghệ sĩ
    ARTIST_ALREADY_EXISTS(5300, "Nghệ sĩ đã tồn tại");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}