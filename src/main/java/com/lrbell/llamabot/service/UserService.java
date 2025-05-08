package com.lrbell.llamabot.service;

import com.lrbell.llamabot.dto.UserDto;
import com.lrbell.llamabot.model.User;
import com.lrbell.llamabot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor/
     *
     * @param userRepository
     * @param passwordEncoder
     */
    @Autowired
    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        // Encode password and save
        final String encodedPassword = passwordEncoder.encode(request.password());
        final User user = new User(request.username(), request.email(), encodedPassword);
        return userRepository.save(user);
    }
}
