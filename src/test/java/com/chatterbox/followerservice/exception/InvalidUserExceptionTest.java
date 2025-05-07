package com.chatterbox.followerservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class InvalidUserExceptionTest {

    @Test
    void instance() {
        String errorMessage = "message";
        InvalidUserException exception = new InvalidUserException(errorMessage);
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}
