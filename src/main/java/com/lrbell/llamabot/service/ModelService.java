package com.lrbell.llamabot.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for interfacing with model to send/receive chat messages.
 */
@Service
public class ModelService {

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
