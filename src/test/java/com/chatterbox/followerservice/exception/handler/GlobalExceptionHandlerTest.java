package com.chatterbox.followerservice.exception.handler;

import com.chatterbox.followerservice.exception.InvalidUserException;
import com.chatterbox.followerservice.exception.MandatoryFieldException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleMandatoryFieldException() {
        String errorMessage = "Username cannot be null or blank";
        MandatoryFieldException exception = new MandatoryFieldException(errorMessage);

        ResponseEntity<Map<String, Object>> response = handler.handleMandatoryField(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("message", errorMessage);
        assertThat(response.getBody()).containsEntry("status", 400);
    }

    @Test
    void shouldHandleInvalidUserException() {
        String errorMessage = "User not found";
        InvalidUserException exception = new InvalidUserException(errorMessage);

        ResponseEntity<Map<String, Object>> response = handler.handleInvalidUserException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("message", errorMessage);
        assertThat(response.getBody()).containsEntry("status", 404);
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new RuntimeException("Something unexpected");

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("message", "An unexpected error occurred.");
        assertThat(response.getBody()).containsEntry("status", 500);
    }
}
