package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.config.JwtUtil;
import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.PageResponseDTO;
import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.dto.users.UserResponseDTO;
import com.spotify.spotify_backend.model.Users;
import com.spotify.spotify_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/user")
    public List<Users> user() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/status")

    public ApiResponse<PageResponseDTO<UserResponseDTO>> getAllUsersByStatusPaginated(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "userName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "status", required = false) Boolean status) {

        PageResponseDTO<UserResponseDTO> response = userService.getAllUsersPaginated(
                pageNo, pageSize, sortBy, sortDir, status);

        return ApiResponse.<PageResponseDTO<UserResponseDTO>>builder()
                .code(1000)
                .message("Lấy danh sách user thành công")
                .result(response)
                .build();
    }

    @GetMapping("/user/{userId}")
    public Users getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/user")
    public ApiResponse<Users> createUser(@RequestBody @Valid CreateUserDTO user) {
        ApiResponse<Users> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(user));
        return apiResponse;
    }

    @GetMapping("/user/me")
    public Users getCurrentUser(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        return userService.getUserById(userId);
    }
}