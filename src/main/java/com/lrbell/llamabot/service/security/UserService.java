package com.lrbell.llamabot.service.security;

import com.lrbell.llamabot.api.dto.UserDto;
import com.lrbell.llamabot.persistence.model.Role;
import com.lrbell.llamabot.persistence.model.User;
import com.lrbell.llamabot.persistence.model.enums.AuthProvider;
import com.lrbell.llamabot.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * The JPA repository for persistence.
     */
    private final UserRepository userRepository;

    /**
     * The service for managing roles.
     */
    private final RoleService roleService;

    /**
     * Password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor
     *
     * @param userRepository
     * @param passwordEncoder
     * @param roleService
     */
    @Autowired
    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder,
                       final RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    /**
     * Registers a new user.
     *
     * @param request The {@link UserDto.RegisterRequest} for the user creation.
     * @return The user.
     */
    @Transactional
    public User register(final UserDto.RegisterRequest request) {

        // Check if username/email exists
        userRepository.findByUsername(request.username())
                .ifPresent(u -> { throw new IllegalArgumentException(String.format("username %s already in use", u.getUsername())); });
        userRepository.findByEmail(request.email())
                .ifPresent(u -> { throw new IllegalArgumentException(String.format("email %s already in use", u.getEmail())); });

        // Get roles/validate they exist
        final Set<Role> roles = roleService.getRolesFromNameSet(request.roles());

        // Encode password
        final String encodedPassword = passwordEncoder.encode(request.password());

        final User user = new User(
                request.username(),
                request.email(),
                encodedPassword,
                AuthProvider.LOCAL,
                roles
        );
        return userRepository.save(user);
    }

    /**
     * Get user by ID.
     *
     * @param userId
     * @return the user
     */
    public Optional<User> getById(final String userId) {
        return userRepository.findById(userId);
    }
}
