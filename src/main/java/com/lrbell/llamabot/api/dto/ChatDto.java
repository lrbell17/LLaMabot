package com.lrbell.llamabot.api.dto;

import com.lrbell.llamabot.persistence.model.ChatMessage;

import java.util.List;

public class ChatDto {

    public record ChatHistoryPageResponse(
            List<ChatMessage> messages,
            int page,
            int size,
            int totalPages,
            long totalElements
    ) {}
}
