package com.chatterbox.followerservice.kafka;

import com.chatterbox.followerservice.mapper.ObjectJsonMapper;
import com.chatterbox.followerservice.model.FollowRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
/**
 * Send new follow events to Kafka topic
 */
public class FollowEventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired private ObjectJsonMapper mapper;

    @Value("${spring.kafka.follow-events-topic-name}")
    private String followEventsTopicName;

    /**
     * Send follow event to Kafka broker
     */
    public void sendPostCreatedEvent(FollowRequest followRequest) {
        kafkaTemplate.send(followEventsTopicName, mapper.followEventToJson(followRequest));
    }
}
