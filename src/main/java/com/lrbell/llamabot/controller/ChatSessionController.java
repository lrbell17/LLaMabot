package com.lrbell.llamabot.controller;

import com.lrbell.llamabot.model.ChatSession;
import com.lrbell.llamabot.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller for managing chat sessions.
 */
@Controller
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    /**
     * Constructor.
     *
     * @param chatSessionService
     */
    @Autowired
    public ChatSessionController(final ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    /**
     * Mutation for adding a chat session.
     *
     * @param userId
     * @return the session.
     */
    @MutationMapping
    public ChatSession startChatSession(@Argument final String userId) {
        return chatSessionService.startChatSession(userId);
    }

    /**
     * Mutation for incrementing the updatedAt time of a chat session.
     *
     * @param sessionId
     */
    @MutationMapping
    public Boolean updateChatSessionTimestamp(@Argument final String sessionId) {
        chatSessionService.updateChatSessionTimestamp(sessionId);
        return true;
    }
}
