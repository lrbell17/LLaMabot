package com.lrbell.llamabot.service.security.oauth;

import com.lrbell.llamabot.persistence.model.User;
import com.lrbell.llamabot.persistence.repository.UserRepository;
import com.lrbell.llamabot.service.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Repository for accessing users.
     */
    private final UserRepository userRepository;

    /**
     * Provider for JWT tokens.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor.
     *
     * @param userRepository
     * @param jwtTokenProvider
     */
    @Autowired
    public OAuthLoginSuccessHandler(final UserRepository userRepository, final JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {
        final OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        final String email = oidcUser.getEmail();
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(String.format("User %s not found after OAuth login", email)));

        final String userId = user.getUserId();
        final String token = jwtTokenProvider.createToken(userId);

        final String redirectUrl = UriComponentsBuilder.fromUriString("/")
                .fragment("token=" + token + "&userId=" + userId)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
