package com.lrbell.llamabot.api.controller;

import com.lrbell.llamabot.api.dto.UserDto;
import com.lrbell.llamabot.persistence.model.User;
import com.lrbell.llamabot.service.security.UserService;
import com.lrbell.llamabot.service.security.AuthService;
import com.lrbell.llamabot.service.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

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
     * Jwt token provider.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor.
     *
     * @param userService
     */
    @Autowired
    public AuthController(final UserService userService, final AuthService authService,
                          final JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
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

        final Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());

        final UserDto.RegisterResponse response = new UserDto.RegisterResponse(user.getUserId(), user.getUsername(),
                user.getEmail(), roleNames);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login.
     *
     * @param request
     * @return DTO containing user ID and JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@Valid @RequestBody final UserDto.LoginRequest request) {
        final UserDto.LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
