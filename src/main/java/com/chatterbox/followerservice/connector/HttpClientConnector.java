package com.chatterbox.followerservice.connector;

import com.chatterbox.followerservice.exception.InvalidUserException;
import com.chatterbox.followerservice.mapper.ObjectJsonMapper;
import com.chatterbox.followerservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;
import reactor.util.function.Tuple2;

import java.time.Duration;

/**
 * Communicates with the User Service to verify if a user exists by username.
 * Makes a non-blocking HTTP GET request using Reactor Netty and deserializes the response.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class HttpClientConnector {

    @Value("${follower-service.connector.user-service.get-by-username-endpoint}")
    private String getByUsernameAPIEndpoint;

    private final ObjectJsonMapper mapper;

    public boolean doesUserExist(String username) {
        String uri = buildUri(username);

        Tuple2<HttpClientResponse, String> response;
        try {
            response = HttpClient.create()
                    .headers(h -> h.add("Accept", "application/json"))
                    .get()
                    .uri(uri)
                    .responseSingle((res, content) -> Mono.just(res).zipWith(content.asString()))
                    .block(Duration.ofSeconds(30));
        } catch (Exception ex) {
            log.error("Exception while calling user service at {}: {}", uri, ex.getMessage());
            throw new InvalidUserException("Unable to verify user: " + username);
        }

        if (response == null || response.getT1().status().code() >= 400) {
            log.error("Failed to fetch user '{}'. Status: {}, URI: {}", username,
                    response != null ? response.getT1().status().code() : "null", uri);
            throw new InvalidUserException("User with username " + username + " does not exist");
        }

        User user = mapper.jsonToUser(response.getT2());
        return user != null;
    }

    private String buildUri(String username) {
        return getByUsernameAPIEndpoint + username;
    }
}
