package com.lrbell.llamabot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class ChatSession {

    /**
     * The session ID.
     */
    @Id
    private String sessionId;

    /**
     * The user the session belongs to.
     */
    @Column(nullable = false)
    private String userId;

    /**
     * The session start time.
     */
    @Column(nullable = false)
    private Instant startedAt;

    /**
     * Session update time.
     */
    @Setter
    @Column(nullable = false)
    private Instant updatedAt;

    /**
     * No arg constructor as required by JPA.
     */
    public ChatSession() {
    }

    /**
     * Constructor.
     *
     * @param userId
     */
    public ChatSession(final String userId) {
        final Instant startTime = Instant.now();
        this.sessionId = UUID.randomUUID().toString();
        this.startedAt = startTime;
        this.updatedAt = startTime;
        this.userId = userId;
    }

}
