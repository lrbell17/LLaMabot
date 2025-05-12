package com.lrbell.llamabot.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(indexes = {@Index(name = "idx_userid_updatedat", columnList = "user_id, updated_at DESC")})
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
     * The description of the chat session, indicating what the conversation is about.
     */
    @Column(nullable = false)
    private String description;

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
    protected ChatSession() {
    }

    /**
     * Constructor.
     *
     * @param userId
     */
    public ChatSession(final String userId, final String description) {
        final Instant startTime = Instant.now();
        this.sessionId = UUID.randomUUID().toString();
        this.startedAt = startTime;
        this.updatedAt = startTime;
        this.userId = userId;
        this.description = description;
    }

}
