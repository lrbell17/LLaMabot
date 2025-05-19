package com.lrbell.llamabot.api.dto;

import com.lrbell.llamabot.api.dto.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserDto {

    public record RegisterRequest(
             @Size(min = 8, max = 32, message = "username must be between 8 and 32 chars")
             @NotBlank(message = "username is required") String username,
             @Email(message = "must be a valid email") String email,
             @NotBlank(message = "password is required")
             @StrongPassword String password,
             Set<String> roles
             ) {}

    public record RegisterResponse(String id, String username, String email, Set<String> roles) {}

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
