package com.chatterbox.followerservice.messaging;

import com.chatterbox.followerservice.util.ObjectJsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Sends notification events to a Kafka topic to notify users.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectJsonMapper mapper;
    @Value("${spring.kafka.notification-events-topic-name}")
    String notificationEventsTopicName;

    public void sendNotificationEvent(String username, String message) {
        var payload = mapper.toJson(new NotificationEvent(username, message));
        kafkaTemplate.send(notificationEventsTopicName, payload);
        log.info("Sent notification to {} with payload: {}", username, payload);
    }

    private record NotificationEvent(String username, String message) {}
}
