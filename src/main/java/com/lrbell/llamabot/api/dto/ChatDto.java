package com.lrbell.llamabot.api.dto;

import java.time.Instant;

public class ChatDto {

    public record MessageResponse(
            Instant timestamp,
            String content
    ) {}
}
