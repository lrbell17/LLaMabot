package com.lrbell.llamabot.persistence.model;

import com.lrbell.llamabot.persistence.model.enums.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
    @Column(unique = true)
    @Setter
    private String email;

    /**
     * The password.
     */
    @Column(nullable = true)
    @Setter
    private String password;

    /**
     * The auth provider (oidcc or local)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    /**
     * The roles associated with the user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Setter
    private Set<Role> roles;

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
    public User(final String userName, final String email, final String password,
                final AuthProvider authProvider, final Set<Role> roles) {
        if (authProvider == AuthProvider.LOCAL && (password == null | password.isEmpty())) {
            throw new IllegalArgumentException("Local users must have a password");
        }
        this.username = userName;
        this.email = email;
        this.password = password;
        this.authProvider = authProvider;
        this.roles = roles;
    }
}
