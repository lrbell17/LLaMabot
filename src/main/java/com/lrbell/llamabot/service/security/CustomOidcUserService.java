package com.lrbell.llamabot.service.security;

import com.lrbell.llamabot.persistence.model.User;
import com.lrbell.llamabot.persistence.model.enums.AuthProvider;
import com.lrbell.llamabot.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class CustomOidcUserService extends OidcUserService {

    /**
     * Logger.
     */
    final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);

    /**
     * Repository for persisting and accessing {@link User}s.
     */
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository
     */
    @Autowired
    public CustomOidcUserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OidcUser oidcUser = super.loadUser(userRequest);

        final String email = oidcUser.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_user_info"),
                    "Email not found from provider");
        }

        userRepository.findByEmail(email).orElseGet(() -> {
            logger.info("OIDC user not found in DB, creating: {}", email);
            final User u = new User(
                    email.substring(0, email.indexOf('@')),
                    email,
                    null,
                    AuthProvider.OIDC
            );
            return userRepository.save(u);
        });

        final OidcUserInfo userInfo = oidcUser.getUserInfo();
        return new DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oidcUser.getIdToken(),
                userInfo
        );
    }

}
