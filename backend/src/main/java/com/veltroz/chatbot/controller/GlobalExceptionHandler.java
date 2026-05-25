package com.veltroz.chatbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        logger.warn("API error: {}", ex.getMessage(), ex);
        Map<String, Object> body = new HashMap<>();
        body.put("status", ex.getStatusCode().value());
        body.put("error", ex.getStatusCode().getClass());
        body.put("message", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("validationErrors", errors);
        return body;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnknownException(Exception ex) {
        logger.error("Unexpected error handling API request", ex);
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        body.put("message", "An unexpected error occurred. Check server logs for details.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
