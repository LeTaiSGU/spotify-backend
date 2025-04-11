package com.spotify.spotify_backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(9999, "Lỗi không xác định"),
    BAD_REQUEST(400, "Yêu cầu không hợp lệ"),
    NOT_FOUND(404, "Không tìm thấy tài nguyên"),
    USER_EXISTED(5001, "User is existed"),
    USER_NOT_FOUND(5000, "User không tồn tại"),
    USER_INVALID(5002, "User đã có hoặc không hợp lệ"),
    PASSWORD_INVALID(5003, "Mật khẩu không hợp lệ"),
    EMAIL_EXISTED(5004, "Email đã tồn tại"),
    SONG_CREATE_FAILED(5005, "Khong tao duoc song"),
    RESOURCE_NOT_FOUND(4004, "Không tìm thấy tài nguyên"),
    VALIDATION_ERROR(5100, "Dữ liệu không hợp lệ"),
    RESOURCE_ALREADY_EXISTS(5007, "Tài nguyên đã tồn tại"),
    SONG_FILE_UPLOAD_FAILED(5006, "Khong upload duoc file"),
    SONG_IMG_UPLOAD_FAILED(5007, "Khong upload duoc hinh"),
    ARTIST_EXISTED(5006, "Artist đã tồn tại"),
    SONG_NOT_FOUND(5007, "Song không tồn tại");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
