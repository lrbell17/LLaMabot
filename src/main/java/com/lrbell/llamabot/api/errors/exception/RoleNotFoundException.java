package com.lrbell.llamabot.api.errors.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(final String message) {
        super(message);
    }
}
