package com.chatterbox.followerservice.messaging;

import com.chatterbox.followerservice.util.ObjectJsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FollowerEventProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectJsonMapper mapper;
    private FollowerEventProducer producer;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        mapper = mock(ObjectJsonMapper.class);

        producer = new FollowerEventProducer(kafkaTemplate, mapper);
        producer.followEventsTopicName = "chatterbox-follow-events";}

    @Test
    void shouldSendFollowEventToKafkaTopic() {
        String follower = "alice";
        String followee = "bob";
        String expectedPayload = "{\"followerUsername\":\"alice\",\"followeeUsername\":\"bob\"}";

        when(mapper.toJson(any())).thenReturn(expectedPayload);

        producer.sendFollowEvent(follower, followee);

        verify(mapper).toJson(any());
        verify(kafkaTemplate).send(eq("chatterbox-follow-events"), eq(expectedPayload));
    }
}
