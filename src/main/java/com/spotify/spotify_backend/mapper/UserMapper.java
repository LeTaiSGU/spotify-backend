package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.dto.users.UpdateRequest;
import com.spotify.spotify_backend.dto.users.UserResponseDTO;
import com.spotify.spotify_backend.model.Users;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users toUsers(CreateUserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateRequest dto, @MappingTarget Users entity);

    UserResponseDTO toDto(Users user);
}
