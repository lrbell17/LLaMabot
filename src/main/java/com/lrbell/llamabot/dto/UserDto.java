package com.lrbell.llamabot.dto;

public class UserDto {

    public record RegisterRequest(String username, String email, String password) {

    }

    public record RegisterResponse(String id, String username, String email) {

    }


}
