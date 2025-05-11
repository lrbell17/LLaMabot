package com.lrbell.llamabot.api.dto;

import com.lrbell.llamabot.persistence.model.ChatSession;
import java.util.List;

public class ChatSessionDto {

    public record SessionPageResponse(
        List<ChatSession> sessions,
        int page,
        int size,
        int totalPages,
        long totalElements
    ) {}
}
