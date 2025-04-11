package com.spotify.spotify_backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(9999, "Lỗi không xác định"),
    USER_EXISTED(5001, "User is existed"),
    USER_NOT_FOUND(5000, "User không tồn tại"),
    USER_INVALID(5002, "User đã có hoặc không hợp lệ"),
    PASSWORD_INVALID(5003, "Mật khẩu không hợp lệ"),
    EMAIL_EXISTED(5004, "Email đã tồn tại"),
    SONG_CREATE_FAILED(5005, "Khong tao duoc song"),
    RESOURCE_NOT_FOUND(4004, "Không tìm thấy tài nguyên"),
    ARTIST_EXISTED(5006, "Artist đã tồn tại"),
    VALIDATION_ERROR(5100, "Dữ liệu không hợp lệ"),
    RESOURCE_ALREADY_EXISTS(5007, "Tài nguyên đã tồn tại");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
