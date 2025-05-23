package com.lrbell.llamabot.service.security.userdetails;

import com.lrbell.llamabot.api.errors.exception.UserNotFoundException;
import com.lrbell.llamabot.persistence.model.User;
import com.lrbell.llamabot.persistence.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * User repository for querying the database.
     */
    final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository
     */
    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("username %s not found", username)));

        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(final String userId) throws UsernameNotFoundException {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user ID %s not found", userId)));

        return new CustomUserDetails(user);

    }
}
