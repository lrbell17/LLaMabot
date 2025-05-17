package com.lrbell.llamabot.persistence.model;

import com.lrbell.llamabot.persistence.model.enums.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "users")
public class User {

    /**
     * The user ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    /**
     * The unique username.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * The unique email.
     */
    @Column(unique = true, nullable = false)
    @Setter
    private String email;

    /**
     * The password.
     */
    @Column(nullable = true)
    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    /**
     * Default constructor required by Spring JPA.
     */
    public User() {
    }

    /**
     * Constructor.
     *
     * @param userName
     * @param email
     * @param password
     */
    public User(final String userName, final String email, final String password, final AuthProvider authProvider) {
        if (authProvider == AuthProvider.LOCAL && (password == null | password.isEmpty())) {
            throw new IllegalArgumentException("Local users must have a password");
        }
        this.username = userName;
        this.email = email;
        this.password = password;
        this.authProvider = authProvider;
    }
}
