package com.lrbell.llamabot.service.security;

import com.lrbell.llamabot.api.dto.UserDto;
import com.lrbell.llamabot.persistence.repository.UserRepository;
import com.lrbell.llamabot.service.security.jwt.JwtTokenProvider;
import com.lrbell.llamabot.service.security.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service for user authentication.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param authenticationManager
     * @param jwtTokenProvider
     * @param userRepository
     */
    @Autowired
    public AuthService(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider,
                       final UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
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
        final CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
        final String userId = principal.getId();

        final String token = jwtTokenProvider.createToken(userId);
        return new UserDto.LoginResponse(userId, token, JwtTokenProvider.TOKEN_TYPE);
    }
}
