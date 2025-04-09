package com.spotify.spotify_backend.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDTO {

    @Size(min = 8,message = "USER_INVALID")
    private String userName;

    @Size(min = 8, message = "PASSWORD_INVALID")
    private String passHash;

    @Email
    private String email;
    private LocalDate dob;
    private boolean isPremium = false;
}
