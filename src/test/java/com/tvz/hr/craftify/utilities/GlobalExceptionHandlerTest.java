package com.tvz.hr.craftify.utilities;

import com.tvz.hr.craftify.utilities.*;
import com.tvz.hr.craftify.utilities.exceptions.*;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @Test
    void handleEntityNotFoundException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found", new RuntimeException("Entity not found"));

        ResponseEntity<ErrorResponse> responseEntity = handler.handleEntityNotFoundException(exception, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Entity not found", responseEntity.getBody().getMessage());
    }

    @Test
    void handleIllegalStateException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalStateException exception = new IllegalStateException("Illegal state");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleIllegalStateException(exception, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Illegal state", responseEntity.getBody().getMessage());
    }

    @Test
    void handleDatabaseOperationException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        DatabaseOperationException exception = new DatabaseOperationException("Database operation failed", new Throwable());

        ResponseEntity<ErrorResponse> responseEntity = handler.handleDatabaseOperationException(exception, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Database operation failed", responseEntity.getBody().getMessage());
    }

    @Test
    void handleApplicationException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ApplicationException exception = new ApplicationException("Application error occurred", new Throwable());

        ResponseEntity<ErrorResponse> responseEntity = handler.handleApplicationException(exception, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Application error occurred", responseEntity.getBody().getMessage());
    }

    @Test
    void handleException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleException(exception, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An unexpected error occurred", responseEntity.getBody().getMessage());
    }

    @Test
    void handleMalformedJwtException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MalformedJwtException exception = new MalformedJwtException("Malformed JWT");

        ResponseEntity<Object> responseEntity = handler.handleMalformedJwtException(exception, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid JWT token", responseEntity.getBody());
    }

    @Test
    void handleTokenExpiredException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        TokenExpiredException exception = new TokenExpiredException("Token expired", new RuntimeException("Token expired due to inactivity"));

        ResponseEntity<ErrorResponse> responseEntity = handler.handleTokenExpiredException(exception, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Token expired", responseEntity.getBody().getMessage());
    }
}
