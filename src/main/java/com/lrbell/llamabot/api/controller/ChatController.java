package com.lrbell.llamabot.api.controller;

import com.lrbell.llamabot.api.dto.ChatDto;
import com.lrbell.llamabot.persistence.model.ChatMessage;
import com.lrbell.llamabot.service.MessageService;
import com.lrbell.llamabot.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
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
    @QueryMapping
    @PreAuthorize("#userId == principal.id")
    public ChatDto.MessageResponse chat(@Argument final String sessionId, @Argument final String userId,
                                        @Argument final String message) {
        final ChatMessage response = messageService.sendMessage(sessionId, message);

        return new ChatDto.MessageResponse(response.getTimestamp(), response.getContent());
    }
}
