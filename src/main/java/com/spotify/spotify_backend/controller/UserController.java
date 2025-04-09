package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.users.CreateUserDTO;
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

    @GetMapping("/user")
    public List<Users> user() {
        return userService.getAllUsers();
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

}
