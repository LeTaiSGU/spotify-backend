package com.spotify.spotify_backend.controller;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.users.loginRequest;
//import com.spotify.spotify_backend.service.AuthenicateService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spotify.spotify_backend.dto.users.loginResponse;

@RestController
@RequestMapping("/auth")
//@RequiredArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenicateController {
//    @Autowired
//    AuthenicateService authenicateService;
//
//    @PostMapping("/login")
//    ApiResponse<loginResponse> login(@RequestBody loginRequest loginRequest) {
//        boolean result = authenicateService.authenicatUser(loginRequest);
//        return ApiResponse.<loginResponse>builder().
//                code(1000).
//                result(loginResponse.builder().isAuthenicated(result).build()).build();
//
//    }
}
