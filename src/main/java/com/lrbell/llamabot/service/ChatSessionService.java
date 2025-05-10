package com.lrbell.llamabot.service;

import com.lrbell.llamabot.errors.exception.ChatSessionNotFoundException;
import com.lrbell.llamabot.model.ChatSession;
import com.lrbell.llamabot.repository.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatSessionService {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ChatSessionService.class);

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
     * Get paginated chat sessions for a user.
     *
     * @param userId
     * @param page
     * @param size
     * @return A page of chat sessions.
     */
    public Page<ChatSession> getSessions(final String userId, final int page, final int size) {
        return chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(userId, PageRequest.of(page, size));
    }

    /**
     * Set the updatedAt for a session to the current time.
     *
     * @param sessionId
     */
    public void updateChatSessionTimestamp(final String sessionId) {
        final int rowsUpdated = chatSessionRepository.updateUpdatedAtById(sessionId, Instant.now());
        if (rowsUpdated == 0) {
            final ChatSessionNotFoundException ex = new ChatSessionNotFoundException(
                    String.format("Chat session with ID %s not found", sessionId)
            );
            logger.warn(String.valueOf(ex));
            throw ex;
        }
    }
}
