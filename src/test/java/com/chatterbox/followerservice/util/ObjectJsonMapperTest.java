package com.chatterbox.followerservice.util;

import com.chatterbox.followerservice.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ObjectJsonMapper}
 */
class ObjectJsonMapperTest {

    private ObjectJsonMapper jsonMapper;

    @BeforeEach
    void setUp() {
        jsonMapper = new ObjectJsonMapper(new ObjectMapper());
    }

    @Test
    void shouldSerializeValidObjectToJson() {
        User user = new User("123", "jdoe", "John", "Doe", "john@example.com");

        String json = jsonMapper.toJson(user);

        assertThat(json).contains("\"id\":\"123\"");
        assertThat(json).contains("\"userName\":\"jdoe\"");
    }

    @Test
    void shouldReturnEmptyJsonOnSerializationFailure() {
        Object badObject = new Object() {
            // Jackson can't serialize cyclic references
            public Object self = this;
        };

        String json = jsonMapper.toJson(badObject);

        assertThat(json).isEqualTo("{}"); // fallback behavior
    }

    @Test
    void shouldDeserializeValidJsonToUser() {
        String json = """
                {
                  "id": "abc",
                  "userName": "jane",
                  "firstName": "Jane",
                  "lastName": "Doe",
                  "email": "jane@example.com"
                }
                """;

        User user = jsonMapper.jsonToUser(json);

        assertThat(user).isNotNull();
        assertThat(user.getUserName()).isEqualTo("jane");
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void shouldReturnNullForInvalidJsonDeserialization() {
        String invalidJson = "this is not valid";

        User user = jsonMapper.jsonToUser(invalidJson);

        assertThat(user).isNull();
    }
}
