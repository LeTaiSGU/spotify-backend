package com.spotify.spotify_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotify.spotify_backend.dto.ApiResponse;
import com.spotify.spotify_backend.dto.search.searchResponse;
import com.spotify.spotify_backend.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping()
    public ApiResponse<searchResponse> searchMethod(@RequestParam String keyword) {
        searchResponse result = searchService.searchKeyWord(keyword);
        return ApiResponse.<searchResponse>builder()
                .message("Search results fetched successfully")
                .result(result)
                .build();
    }

}
