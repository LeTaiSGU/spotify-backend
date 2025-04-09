package com.spotify.spotify_backend.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateRequest {
    @Email
    String email;
    @Size(min = 8,message = "Password has a least 8 character")
    String password;
    LocalDate dob;
    boolean isPremium;
}
