package com.lrbell.llamabot.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for validating JWT token.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    /**
     * Jwt token provider.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Service for getting user details.
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructor.
     *
     * @param jwtTokenProvider
     * @param userDetailsService
     */
    public JwtTokenFilter(final JwtTokenProvider jwtTokenProvider, final UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(JwtTokenProvider.TOKEN_TYPE)) {
            final String token = header.substring((JwtTokenProvider.TOKEN_TYPE + " ").length());

            // Validate JWT and store user info in security context
            if (jwtTokenProvider.validateToken(token)) {
                final String username = jwtTokenProvider.getUsernameFromToken(token);
                final var userDetails = userDetailsService.loadUserByUsername(username);
                final var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // Continue chain
        filterChain.doFilter(request, response);
    }
}
