package com.lrbell.llamabot.controller;

import com.lrbell.llamabot.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller for interfacing with chat bot.
 */
@Controller
public class ChatController {

    /**
     * The chat service.
     */
    private final ChatService chatService;

    /**
     * Constructor.
     *
     * @param chatService
     */
    @Autowired
    public ChatController(final ChatService chatService) {
        this.chatService = chatService;
    }


    /**
     * GraphQL request to send message to chat service and get response.
     * @param message
     * @return The response from the {@link ChatService}
     */
    @QueryMapping
    public String chat(@Argument final String message) {
        return chatService.sendMessage(message);
    }
}
