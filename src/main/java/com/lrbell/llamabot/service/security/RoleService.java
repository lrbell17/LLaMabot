package com.lrbell.llamabot.service.security;

import com.lrbell.llamabot.api.errors.exception.RoleNotFoundException;
import com.lrbell.llamabot.persistence.model.Role;
import com.lrbell.llamabot.persistence.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    /**
     * The role repository.
     */
    private final RoleRepository roleRepository;

    /**
     * Constructor.
     *
     * @param roleRepository
     */
    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Get a role from the DB by name.
     *
     * @param roleName
     * @return the role
     * @throws EntityNotFoundException if the role is not found.
     */
    public Role getRoleByName(final String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> {
            final String message = String.format("Role %s not found", roleName);
            logger.warn(message);
            throw new RoleNotFoundException(message);
        });
    }

    /**
     * Turns a set of role names into a set of roles.
     *
     * @param roleNames
     * @return The set of {@link Role}
     */
    public Set<Role> getRolesFromNameSet(final Set<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> getRoleByName(roleName))
                .collect(Collectors.toSet());
    }

}
