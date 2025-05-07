package com.chatterbox.followerservice.messaging;

import com.chatterbox.followerservice.util.ObjectJsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Sends follow events to a Kafka topic to notify other services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectJsonMapper mapper;
    @Value("${spring.kafka.follow-events-topic-name}")
    private String followEventsTopicName;

    public void sendFollowEvent(String followerUsername, String followeeUsername) {
        var payload = mapper.toJson(new FollowEvent(followerUsername, followeeUsername));
        kafkaTemplate.send(followEventsTopicName, payload);
        log.info("Sent Kafka follower event: {}", payload);
    }

    private record FollowEvent(String followerUsername, String followeeUsername) {}
}
