package com.lrbell.llamabot.dto;

import com.lrbell.llamabot.model.ChatSession;
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
