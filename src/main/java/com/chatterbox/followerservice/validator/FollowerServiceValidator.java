package com.chatterbox.followerservice.validator;

import com.chatterbox.followerservice.connector.HttpClientConnector;
import com.chatterbox.followerservice.exception.InvalidUserException;
import com.chatterbox.followerservice.exception.MandatoryFieldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerServiceValidator {

    private final HttpClientConnector clientConnector;

    /**
     * Validates that the username is non-empty and exists in the User Service.
     *
     * @param username the username to validate
     * @throws MandatoryFieldException if username is null or empty
     * @throws InvalidUserException if user does not exist in the user service
     */
    public void validateUser(String username) {
        if (!StringUtils.hasText(username)) {
            log.warn("Validation failed: empty or null username");
            throw new MandatoryFieldException("Username cannot be empty or null");
        }
        if (!clientConnector.doesUserExist(username)) {
            log.warn("User '{}' does not exist in ChatterBox platform", username);
            throw new InvalidUserException("User does not exist in ChatterBox platform: " + username);
        }
        log.info("User '{}' passed validation", username);
    }
}
