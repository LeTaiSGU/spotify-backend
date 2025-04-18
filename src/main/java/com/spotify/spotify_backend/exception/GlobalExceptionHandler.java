package com.spotify.spotify_backend.exception;

import com.spotify.spotify_backend.dto.ApiResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingException(RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    // @ExceptionHandler(value = RuntimeException.class)
    // ResponseEntity<ApiResponse> handlingException(RuntimeException exception) {
    // ApiResponse apiResponse = new ApiResponse();
    // apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
    // apiResponse.setMessage(ErrorCode.USER_EXISTED.getMessage());
    // return ResponseEntity.badRequest().body(apiResponse);
    // }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        HttpStatus status = switch (errorCode.getCode()) {
            case 4004 -> HttpStatus.NOT_FOUND;
            case 5000, 5001, 5002, 5003, 5004, 5005, 5006 -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(status).body(apiResponse);
    }
    // @ExceptionHandler(value = AppException.class)
    // ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
    // ErrorCode errorCode = exception.getErrorCode();
    // ApiResponse apiResponse = new ApiResponse();

    // apiResponse.setCode(errorCode.getCode());
    // apiResponse.setMessage(errorCode.getMessage());
    // return ResponseEntity.badRequest().body(apiResponse);
    // }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // @ExceptionHandler(value = MethodArgumentNotValidException.class)
    // ResponseEntity<ApiResponse>
    // handlingValidation(MethodArgumentNotValidException exception) {
    // String enumKey = exception.getFieldError().getDefaultMessage();
    // ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
    // try {
    // errorCode = ErrorCode.valueOf(enumKey);
    // } catch (IllegalArgumentException e) {
    // e.printStackTrace();
    // }
    // ApiResponse apiResponse = new ApiResponse();
    // apiResponse.setCode(errorCode.getCode());
    // apiResponse.setMessage(errorCode.getMessage());
    // return ResponseEntity.badRequest().body(apiResponse);
    // }

}
