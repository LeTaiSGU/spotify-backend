package com.spotify.spotify_backend.mapper;

import com.spotify.spotify_backend.dto.users.CreateUserDTO;
import com.spotify.spotify_backend.model.Users;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-09T20:49:46+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Users toUsers(CreateUserDTO createUserDTO) {
        if ( createUserDTO == null ) {
            return null;
        }

        Users users = new Users();

        users.setDob( createUserDTO.getDob() );
        users.setEmail( createUserDTO.getEmail() );
        users.setPassHash( createUserDTO.getPassHash() );
        users.setPremium( createUserDTO.isPremium() );
        users.setUserName( createUserDTO.getUserName() );

        return users;
    }
}
