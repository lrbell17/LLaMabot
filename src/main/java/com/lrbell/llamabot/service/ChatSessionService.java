package com.lrbell.llamabot.service;

import com.lrbell.llamabot.persistence.model.ChatSession;
import com.lrbell.llamabot.persistence.repository.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatSessionService {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ChatSessionService.class);

    /**
     * The service for managing chat messages.
     */
    private final MessageService messageService;

    /**
     * The JPA repository for persistence.
     */
    private final ChatSessionRepository chatSessionRepository;

    /**
     * Constructor.
     *
     * @param messageService
     * @param repo
     */
    @Autowired
    public ChatSessionService(final MessageService messageService, final ChatSessionRepository repo) {
        this.messageService = messageService;
        this.chatSessionRepository = repo;
    }

    /**
     * Start a new chat session.
     *
     * @param userId
     * @return the session.
     */
    @Transactional
    public ChatSession startChatSession(final String userId, final String message) {

        // create session
        final ChatSession chatSession = chatSessionRepository.save(
                new ChatSession(userId, getDescriptionFromMessage(message, 32)));

        // send initial message
        messageService.sendMessage(chatSession.getSessionId(), message);

        return chatSession;
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
     * Get the session's description from the initial message, trimming if necessary.
     *
     * @param message
     * @param maxLength
     * @return The description.
     */
    private String getDescriptionFromMessage(final String message, final int maxLength) {
        if (message.length() > maxLength) {
            return message.substring(0, maxLength - 1) + "...";
        }
        return message;
    }
}
