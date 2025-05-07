package com.chatterbox.followerservice.connector;

import com.chatterbox.followerservice.dto.User;
import com.chatterbox.followerservice.exception.InvalidUserException;
import com.chatterbox.followerservice.util.ObjectJsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.netty.http.client.HttpClientResponse;
import reactor.util.function.Tuple2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO: Fix broken tests
@ExtendWith(MockitoExtension.class)
class HttpClientConnectorTest {

    @Mock
    private HttpClientConnector connector;
    private ObjectJsonMapper mockMapper;

    @BeforeEach
    void setUp() {
        mockMapper = mock(ObjectJsonMapper.class);
        connector = new HttpClientConnector(mockMapper);
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        String username = "validuser";
        String fakeJson = "{\"id\":\"123\",\"userName\":\"validuser\"}";

        User user = new User("123", "validuser", "John", "Doe", "john@example.com");
        when(mockMapper.jsonToUser(fakeJson)).thenReturn(user);

        // Simulate internal method to be mocked if refactored
        var response = mock(Tuple2.class);
        HttpClientResponse httpResponse = mock(HttpClientResponse.class);
        when(httpResponse.status()).thenReturn(io.netty.handler.codec.http.HttpResponseStatus.OK);
        when(response.getT1()).thenReturn(httpResponse);
        when(response.getT2()).thenReturn(fakeJson);

        boolean exists = connector.doesUserExist(username);

        // Since we mocked the mapper, assume parsing works
        assertThat(exists).isTrue();
        verify(mockMapper).jsonToUser(fakeJson);
    }

    @Test
    void shouldThrowExceptionWhenHttpFails() {
        String username = "invaliduser";

        assertThrows(InvalidUserException.class, () -> {
            connector.doesUserExist(username); // would fail due to real HttpClient usage
        });
    }
}
