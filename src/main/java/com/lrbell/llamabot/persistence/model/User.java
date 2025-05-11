package com.lrbell.llamabot.persistence.model;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    @Setter
    private String password;

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
    public User(final String userName, final String email, final String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
    }
}
