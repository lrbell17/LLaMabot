package com.lrbell.llamabot.service.security.oauth;

import com.lrbell.llamabot.persistence.model.Role;
import com.lrbell.llamabot.persistence.model.User;
import com.lrbell.llamabot.persistence.model.enums.AuthProvider;
import com.lrbell.llamabot.persistence.repository.UserRepository;
import com.lrbell.llamabot.service.security.RoleService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


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
     * Service for managing roles.
     */
    private final RoleService roleService;

    /**
     * Constructor.
     *
     * @param userRepository
     * @param roleService
     */
    @Autowired
    public CustomOidcUserService(final UserRepository userRepository, final RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OidcUser oidcUser = super.loadUser(userRequest);

        final String email = oidcUser.getEmail();
        if (email == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_user_info"),
                    "Email not found from provider");
        }

        // Get roles
        final String accessTokenValue = userRequest.getAccessToken().getTokenValue();
        final Set<String> roleNames = extractRolesFromAccessToken(accessTokenValue);
        final Set<Role> roles = roleService.getRolesFromNameSet(roleNames);

        final User user = userRepository.findByEmail(email).map(existingUser -> {

            // update roles for existing user, if necessary
            if (!existingUser.getRoles().equals(roles)) {
                logger.info("Updating roles for existing OIDC user {}", email);
                existingUser.setRoles(roleService.getRolesFromNameSet(roleNames));
                return userRepository.save(existingUser);
            }
            return existingUser;
        }).orElseGet(() -> { // if the user doesn't exist, create one
            logger.info("OIDC user not found in DB, creating: {}", email);
            final User u = new User(
                    email.substring(0, email.indexOf('@')),
                    email,
                    null,
                    AuthProvider.OIDC,
                    roles
                    );
                    return userRepository.save(u);
        });

        final List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());

        final OidcUserInfo userInfo = oidcUser.getUserInfo();
        return new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                userInfo
        );
    }

    /**
     * Get roles from access token.
     *
     * @param accessTokenValue
     * @return The set of roles
     */
    private Set<String> extractRolesFromAccessToken(final String accessTokenValue) {
        try {
            final JWT jwt = JWTParser.parse(accessTokenValue);
            final Map<String, Object> claims = jwt.getJWTClaimsSet().getClaims();

            final Object realmAccessObj = claims.get("realm_access");
            if (realmAccessObj instanceof Map<?, ?> realmAccess) {
                final Object rolesObj = realmAccess.get("roles");
                if (rolesObj instanceof Collection<?> rolesCollection) {
                    return rolesCollection.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .collect(Collectors.toSet());
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to parse access token for roles", e);
        }
        return Set.of();
    }
}
