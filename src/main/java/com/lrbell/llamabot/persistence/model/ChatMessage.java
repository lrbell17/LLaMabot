package com.lrbell.llamabot.persistence.model;

import com.lrbell.llamabot.persistence.model.enums.Sender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(indexes = {@Index(name = "idx_session_timestamp", columnList = "session_id, timestamp DESC")})
public class ChatMessage {

    /**
     * The message ID.
     */
    @Id
    private String messageId;

    /**
     * The chat session.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    /**
     * The timestamp of when the message was sent.
     */
    @Column(nullable = false)
    private Instant timestamp;

    /**
     * The message content.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * The sender (USER or BOT)
     */
    private String sender;

    /**
     * Default constructor required by JPA.
     */
    protected ChatMessage() {}


    /**
     * Constructor.
     *
     * @param session
     * @param content
     * @param sender
     */
    public ChatMessage(final ChatSession session, final String content, final Sender sender) {
        this.messageId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.session = session;
        this.content = content;
        this.sender = sender.name();
    }

    /**
     * Constructor.
     *
     * @param session
     * @param content
     * @param sender
     * @param timestamp
     */
    public ChatMessage(final ChatSession session, final String content, final Sender sender, final Instant timestamp) {
        this.messageId = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.session = session;
        this.content = content;
        this.sender = sender.name();
    }
}
