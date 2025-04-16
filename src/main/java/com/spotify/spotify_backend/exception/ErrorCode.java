package com.spotify.spotify_backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(9999, "Lỗi không xác định"),
    NOT_FOUND(404, "Không tìm thấy tài nguyên"),
    VALIDATION_ERROR(4000, "Dữ liệu không hợp lệ"),
    USER_NOT_FOUND(5000, "Người dùng không tồn tại"),
    USER_EXISTED(5001, "Email đã tồn tại"),
    USER_INVALID(5002, "Thông tin người dùng không hợp lệ"),
    PASSWORD_INVALID(5003, "Mật khẩu không hợp lệ"),
    EMAIL_EXISTED(5004, "Email đã tồn tại"),
    USERNAME_ALREADY_EXISTS(5005, "Tên người dùng đã tồn tại"),
    IDENTIFIER_REQUIRED(5006, "Email hoặc tên người dùng bắt buộc"),
    INVALID_GOOGLE_TOKEN(5100, "Token Google không hợp lệ"),
    GOOGLE_LOGIN_FAILED(5101, "Đăng nhập Google thất bại"),
    RESOURCE_NOT_FOUND(4004, "Không tìm thấy tài nguyên"),
    SONG_CREATE_FAILE(5200, "Không tạo được bài hát"),
    SONG_NOT_FOUND(5201, "Bài hát không tồn tại"),
    USER_NOT_GOOGLE(5204, "Người dùng này không có tài khoản google"),
    SONG_FILE_UPLOAD_FAILED(5202, "Không upload được file bài hát"),
    SONG_IMG_UPLOAD_FAILED(5203, "Không upload được hình bài hát"),
    SONG_ALREADY_EXISTED(5024, "Bài hát đã tồn tại trong playlist"),
    ARTIST_EXISTED(5300, "Nghệ sĩ đã tồn tại"),
    PLAYLIST_NOT_FOUND(5401, "Playlist không tồn tại");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}