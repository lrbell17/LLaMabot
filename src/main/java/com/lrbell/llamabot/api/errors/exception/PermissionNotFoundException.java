package com.lrbell.llamabot.api.errors.exception;

public class PermissionNotFoundException extends RuntimeException {

    public PermissionNotFoundException(final String message) {
        super(message);
    }
}
