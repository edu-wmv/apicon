package com.br.ufba.icon.api.controller;

import com.br.ufba.icon.api.controller.dto.ApiErrorResponse;
import com.br.ufba.icon.api.exceptions.AccessDeniedException;
import com.br.ufba.icon.api.exceptions.DuplicateException;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiErrorResponse(NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleRequestNotValidException(MethodArgumentNotValidException e) {

        List<String> errors = new ArrayList<>();
        e.getBindingResult()
                .getFieldErrors().forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
        e.getBindingResult()
                .getGlobalErrors()
                .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        String message = "Request validation failed: %s".formatted(String.join(", ", errors));

        return ResponseEntity.status(BAD_REQUEST)
                .body(new ApiErrorResponse(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ApiErrorResponse(UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException() {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ApiErrorResponse(UNAUTHORIZED.value(), "Invalid username or password"));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateException(DuplicateException e) {
        return ResponseEntity.status(CONFLICT)
                .body(new ApiErrorResponse(CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleInternalAuthenticationServiceException(Exception e) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ApiErrorResponse(UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknownException(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}
