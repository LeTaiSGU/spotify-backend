package com.spotify.spotify_backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATERIZED(9999, "Lỗi không xác định"),
    USER_EXISTED(5001, "User is existed"),
    USER_NOT_FOUND(5000, "User không tồn tại"),
    USER_INVALID(5002, "User đã có hoặc không hợp lệ"),
    PASSWORD_INVALID(5003, "Mật khẩu không đúng"),
    EMAIL_EXISTED(5004, "Email đã tồn tại"),
    SONG_CREATE_FAILE(5005, "Không tạo được bài hát"),
    ARTIST_EXISTED(5006, "Artist đã tồn tại"),
    USERNAME_ALREADY_EXISTS(5007, "Username đã tồn tại"),
    INVALID_GOOGLE_TOKEN(5008, "Google token không hợp lệ"),
    GOOGLE_LOGIN_FAILED(5009, "Đăng nhập Google thất bại");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}