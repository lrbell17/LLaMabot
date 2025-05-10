package com.lrbell.llamabot.dto;

import com.lrbell.llamabot.dto.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    public record RegisterRequest(
             @Size(min = 8, max = 32, message = "username must be between 8 and 32 chars")
             @NotBlank(message = "username is required") String username,
             @Email(message = "must be a valid email")
             @NotBlank(message = "email is required") String email,
             @NotBlank(message = "password is required")
             @StrongPassword String password) {}

    public record RegisterResponse(String id, String username, String email) {

    }

    public record LoginRequest(
            @NotBlank(message = "username is required") String username,
            @NotBlank(message = "password is required") String password
    ) {}

    public record LoginResponse(
            String userId,
            String token,
            String tokenType
    ) {}

}
