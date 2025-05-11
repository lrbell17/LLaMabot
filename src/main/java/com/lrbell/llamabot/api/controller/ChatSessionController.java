package com.lrbell.llamabot.api.controller;

import com.lrbell.llamabot.api.dto.ChatSessionDto;
import com.lrbell.llamabot.persistence.model.ChatSession;
import com.lrbell.llamabot.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("#userId == principal.id")
    public ChatSession startChatSession(@Argument final String userId) {
        return chatSessionService.startChatSession(userId);
    }

    /**
     * Query for getting chat sessions for a user.
     *
     * @param userId
     * @param page
     * @param size
     * @return A page of sessions.
     */
    @QueryMapping
    @PreAuthorize("#userId == principal.id")
    public ChatSessionDto.SessionPageResponse getChatSessions(@Argument final String userId,
                                          @Argument final int page, @Argument final int size) {
        final Page<ChatSession> result = chatSessionService.getSessions(userId, page, size);

        return new ChatSessionDto.SessionPageResponse(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements()
        );
    }

}
