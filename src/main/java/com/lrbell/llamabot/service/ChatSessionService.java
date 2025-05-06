package com.lrbell.llamabot.service;

import com.lrbell.llamabot.errors.exception.ChatSessionNotFoundException;
import com.lrbell.llamabot.model.ChatSession;
import com.lrbell.llamabot.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatSessionService {

    /**
     * The JPA repository for persistence.
     */
    private final ChatSessionRepository chatSessionRepository;

    /**
     * Constructor.
     *
     * @param repo
     */
    @Autowired
    public ChatSessionService(final ChatSessionRepository repo) {
        this.chatSessionRepository = repo;
    }

    /**
     * Start a new chat session.
     *
     * @param userId
     * @return the session.
     */
    public ChatSession startChatSession(final String userId) {
        final ChatSession chatSession = new ChatSession(userId);
        return chatSessionRepository.save(chatSession);
    }

    /**
     * Set the updatedAt for a session to the current time.
     *
     * @param sessionId
     */
    public void updateChatSessionTimestamp(final String sessionId) {
        final int rowsUpdated = chatSessionRepository.updateUpdatedAtById(sessionId, Instant.now());
        if (rowsUpdated == 0) {
            throw new ChatSessionNotFoundException("Chat session with ID " + sessionId + " not found.");
        }
    }
}
