package com.lrbell.llamabot.api.controller;

import com.lrbell.llamabot.api.dto.ChatDto;
import com.lrbell.llamabot.persistence.model.ChatMessage;
import com.lrbell.llamabot.service.MessageService;
import com.lrbell.llamabot.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

/**
 * Controller for interfacing with chatbot.
 */
@Controller
public class ChatController {

    /**
     * The chat service.
     */
    private final MessageService messageService;

    /**
     * Constructor.
     *
     * @param messageService
     */
    @Autowired
    public ChatController(final MessageService messageService) {
        this.messageService = messageService;
    }


    /**
     * GraphQL request to send message to chat service and get response.
     * @param message
     * @return The response from the {@link ModelService}
     */
    @MutationMapping
    @PreAuthorize("#userId == principal.id")
    public ChatMessage chat(@Argument final String sessionId, @Argument final String userId,
                                        @Argument final String message) {
        return messageService.sendMessage(sessionId, message);
    }

    @QueryMapping
    @PreAuthorize("#userId == principal.id")
    public ChatDto.ChatHistoryPageResponse chatHistory(@Argument final String sessionId, @Argument final String userId,
                                                       @Argument final int page, @Argument final int size) {
        final Page<ChatMessage> result = messageService.getChatHistory(sessionId, page, size);

        return new ChatDto.ChatHistoryPageResponse(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements()
        );
    }
}
