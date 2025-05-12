package com.lrbell.llamabot.service;

import com.lrbell.llamabot.api.errors.exception.ChatSessionNotFoundException;
import com.lrbell.llamabot.persistence.model.ChatMessage;
import com.lrbell.llamabot.persistence.model.ChatSession;
import com.lrbell.llamabot.persistence.model.Sender;
import com.lrbell.llamabot.persistence.repository.ChatMessageRepository;
import com.lrbell.llamabot.persistence.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Service for managing chat messages.
 */
@Service
public class MessageService {

    /**
     * Service for interfacing with model.
     */
    private final ModelService modelService;

    /**
     * Repository for chat messages.
     */
    private final ChatMessageRepository messageRepository;

    /**
     * Repository for chat sessions.
     */
    private final ChatSessionRepository chatSessionRepository;

    /**
     * Constructor.
     *
     * @param modelService
     * @param messageRepository
     * @param chatSessionRepository
     */
    @Autowired
    public MessageService(final ModelService modelService, final ChatMessageRepository messageRepository,
                          final ChatSessionRepository chatSessionRepository) {
        this.modelService = modelService;
        this.messageRepository = messageRepository;
        this.chatSessionRepository = chatSessionRepository;
    }

    /**
     * Send a chat message and store the user and bot's message.
     *
     * @param sessionId
     * @param content
     * @return The bot's response.
     */
    @Transactional
    public ChatMessage sendMessage(final String sessionId, final String content) {

        // Get session by ID
        final ChatSession session = chatSessionRepository.findById(sessionId).orElseThrow(() ->
                new ChatSessionNotFoundException(String.format("Chat session with ID %s not found", sessionId)));

        // Save user message
        final ChatMessage message = new ChatMessage(session, content, Sender.USER);
        messageRepository.save(message);

        // Send user message to model and get response
        final String responseContent = modelService.sendMessage(content);

        // Save response and increment the session's updatedAt timestamp
        final Instant updateAtTs = Instant.now();
        final ChatMessage response = new ChatMessage(session, responseContent, Sender.BOT, updateAtTs);
        messageRepository.save(response);
        chatSessionRepository.updateUpdatedAtById(sessionId, updateAtTs);

        return response;
    }
}
