package com.chatterbox.followerservice.mapper;

import com.chatterbox.followerservice.model.FollowRequest;
import com.chatterbox.followerservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ObjectJsonMapper {

    @Autowired
    ObjectMapper mapper;

    public String followEventToJson(FollowRequest followRequest) {
        var json = "";
        try {
            json = mapper.writeValueAsString(followRequest);
        } catch (JsonProcessingException e) {
            log.error("Unable to map followRequest object to JSON");
        }
        return json;
    }

    public User jsonToUser(String json) {
        try {
            return mapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse JSON to User object", e);
            return null;
        }
    }
}
