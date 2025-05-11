package com.lrbell.llamabot.api.controller;

import com.lrbell.llamabot.api.dto.UserDto;
import com.lrbell.llamabot.persistence.model.User;
import com.lrbell.llamabot.service.UserService;
import com.lrbell.llamabot.service.security.AuthService;
import jakarta.validation.Valid;
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

    /**
     * User service for CRUD operations on users.
     */
    private final UserService userService;

    /**
     * Auth service for user authentication.
     */
    private final AuthService authService;

    /**
     * Constructor.
     *
     * @param userService
     */
    @Autowired
    public AuthController(final UserService userService, final AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * Register a user.
     *
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto.RegisterResponse> register(@Valid @RequestBody final UserDto.RegisterRequest request) {
        final User user = userService.register(request);
        final UserDto.RegisterResponse response = new UserDto.RegisterResponse(user.getUserId(), user.getUsername(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login.
     *
     * @param request
     * @return A temporary JWT used to make requests to other endpoints.
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@Valid @RequestBody final UserDto.LoginRequest request) {
        final UserDto.LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
