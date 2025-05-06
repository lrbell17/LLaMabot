package com.lrbell.llamabot.errors.exception;

import com.lrbell.llamabot.errors.GraphQlResolvableError;
import org.springframework.graphql.execution.ErrorType;

public class ChatSessionNotFoundException extends RuntimeException implements GraphQlResolvableError {
    public ChatSessionNotFoundException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.NOT_FOUND;
    }

}
