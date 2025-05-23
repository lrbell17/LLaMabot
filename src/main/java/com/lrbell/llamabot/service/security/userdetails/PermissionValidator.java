package com.lrbell.llamabot.service.security.userdetails;

import com.lrbell.llamabot.service.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionValidator")
public class PermissionValidator {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(PermissionValidator.class);

    /**
     * Service for getting user info
     */
    private final UserService userService;

    /**
     * Constructor.
     *
     * @param userService
     */
    @Autowired
    public PermissionValidator(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Check if a user has a permission.
     *
     * @param auth
     * @param requiredPermission
     * @return true if the user has the permission, otherwise false
     */
    public boolean hasPermission(final Authentication auth, final String requiredPermission) {

        final Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {

            final String userId = ((CustomUserDetails) principal).getId();

            return userService.getById(userId)
                    .map(u -> u.getRoles().stream()
                            .flatMap(role -> role.getPermissions().stream())
                            .anyMatch(permission -> permission.getPermissionName().equals(requiredPermission)))
                    .orElse(false);
        }
        logger.warn("Auth principal of type {} not supported", principal.getClass());
        return false;
    }
}
