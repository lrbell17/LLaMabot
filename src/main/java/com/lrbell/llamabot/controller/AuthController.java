package com.lrbell.llamabot.controller;

import com.lrbell.llamabot.dto.UserDto;
import com.lrbell.llamabot.model.User;
import com.lrbell.llamabot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user authentication.
 */
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Constructor.
     *
     * @param userService
     */
    @Autowired
    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a user.
     *
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto.RegisterResponse> register(@RequestBody final UserDto.RegisterRequest request) {
        final User user = userService.register(request);
        final UserDto.RegisterResponse response = new UserDto.RegisterResponse(user.getUserId(), user.getUsername(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
