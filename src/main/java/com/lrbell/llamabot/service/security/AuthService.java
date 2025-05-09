package com.lrbell.llamabot.service.security;

import com.lrbell.llamabot.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service for user authentication.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor.
     *
     * @param authenticationManager
     * @param jwtTokenProvider
     */
    @Autowired
    public AuthService(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Login with username/password.
     *
     * @param request
     * @return JWT token
     */
    public UserDto.LoginResponse login(final UserDto.LoginRequest request) {
        final Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        final String token = jwtTokenProvider.createToken(request.username());
        return new UserDto.LoginResponse(token, JwtTokenProvider.TOKEN_TYPE);
    }
}
