package com.lrbell.llamabot.api.errors;


import com.lrbell.llamabot.api.errors.exception.RoleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Error handler for REST API response.
 */
@ControllerAdvice
public class RestErrorHandler {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RestErrorHandler.class);

    /**
     * Handle {@link IllegalArgumentException} as a bad request.
     *
     * @param ex
     * @return 400 response with error message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(final IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handle {@link MethodArgumentNotValidException} as bad request.
     *
     * @param ex
     * @return 400 response with error message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidation(final MethodArgumentNotValidException ex) {
        final List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", errors));
    }

    /**
     * Handle {@link BadCredentialsException} as unauthorized.
     *
     * @param ex
     * @return 401 response with error message.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(final BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handle {@link AuthorizationDeniedException} as forbidden.
     *
     * @param ex
     * @return 403 response with error message.
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(final AuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handle {@link NoResourceFoundException} and {@link NoHandlerFoundException} as not found.
     * @param ex
     * @return 404 response with error message.
     */
    @ExceptionHandler( {NoResourceFoundException.class, NoHandlerFoundException.class, RoleNotFoundException.class })
    public ResponseEntity<Map<String,String>> handleNotFound(final Exception ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handle the rest of exceptions as internal server error.
     *
     * @param ex
     * @return 500 response with error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAll(final Exception ex) {
        logger.error("Internal error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred"));
    }
}
