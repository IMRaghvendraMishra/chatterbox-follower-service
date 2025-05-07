package com.chatterbox.followerservice.validator;

import com.chatterbox.followerservice.connector.HttpClientConnector;
import com.chatterbox.followerservice.exception.InvalidUserException;
import com.chatterbox.followerservice.exception.MandatoryFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link FollowerServiceValidator}
 */
class FollowerServiceValidatorTest {

    private HttpClientConnector mockConnector;
    private FollowerServiceValidator validator;

    @BeforeEach
    void setUp() {
        mockConnector = mock(HttpClientConnector.class);
        validator = new FollowerServiceValidator(mockConnector);
    }

    @Test
    void shouldThrowMandatoryFieldExceptionIfUsernameIsBlank() {
        assertThrows(MandatoryFieldException.class, () -> validator.validateUser("  "));
        assertThrows(MandatoryFieldException.class, () -> validator.validateUser(null));
        assertThrows(MandatoryFieldException.class, () -> validator.validateUser(""));
    }

    @Test
    void shouldThrowInvalidUserExceptionIfUserDoesNotExist() {
        String username = "nonexistent";
        when(mockConnector.doesUserExist(username)).thenReturn(false);

        assertThrows(InvalidUserException.class, () -> validator.validateUser(username));
        verify(mockConnector).doesUserExist(username);
    }

    @Test
    void shouldPassValidationIfUserExists() {
        String username = "validuser";
        when(mockConnector.doesUserExist(username)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateUser(username));
        verify(mockConnector).doesUserExist(username);
    }
}
