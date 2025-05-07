package com.chatterbox.followerservice.util;

import com.chatterbox.followerservice.dto.Post;
import com.chatterbox.followerservice.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class for serializing and deserializing JSON using Jackson.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ObjectJsonMapper {

    private final ObjectMapper objectMapper;

    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object: {}", obj, e);
            return "{}";
        }
    }

    public User jsonToUser(String json) {
        try {
            return objectMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse JSON to User object", e);
            return null;
        }
    }

    public Post jsonToPost(String json) {
        try {
            return objectMapper.readValue(json, Post.class);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse JSON to Post object", e);
            return null;
        }
    }
}
