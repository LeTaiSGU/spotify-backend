package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.dto.users.UpdateRequest;
import com.spotify.spotify_backend.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUsers(CreateUserDTO createUserDTO);
    // void updateUsers(Users users, UpdateRequest updateRequest);
}
