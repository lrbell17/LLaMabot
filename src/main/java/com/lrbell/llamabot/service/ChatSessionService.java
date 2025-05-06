package com.lrbell.llamabot.service;

import com.lrbell.llamabot.model.ChatSession;
import com.lrbell.llamabot.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
