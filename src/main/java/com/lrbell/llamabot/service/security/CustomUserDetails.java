package com.lrbell.llamabot.service.security;

import com.lrbell.llamabot.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    /**
     * The user.
     */
    private final User user;

    /**
     * Constructor.
     *
     * @param user
     */
    public CustomUserDetails(final User user) {
        this.user = user;
    }

    /**
     * Get the user ID.
     *
     * @return the ID.
     */
    public String getId() {
        return user.getUserId();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // not yet supported
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
