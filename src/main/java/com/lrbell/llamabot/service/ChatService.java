package com.lrbell.llamabot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing chat messages.
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    /**
     * The chat client for direct interaction with the model.
     */
    @Autowired
    private ChatClient chatClient;

    /**
     * Send the AI bot a message.
     *
     * @param message The user's message to send to the Llama model
     * @return The bot's response.
     */
    public String sendMessage(final String message) {

        final ChatResponse response = chatClient.call(new Prompt(message));
        return response.getResult().getOutput().getContent();
    }
}
