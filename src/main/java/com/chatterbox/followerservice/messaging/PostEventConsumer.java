package com.chatterbox.followerservice.messaging;

import com.chatterbox.followerservice.dto.Post;
import com.chatterbox.followerservice.service.FollowerService;
import com.chatterbox.followerservice.util.ObjectJsonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class PostEventConsumer {

    @Autowired private final NotificationEventProducer notificationEventProducer;
    @Autowired private final ObjectJsonMapper mapper;
    @Autowired private final FollowerService followerService;

    @KafkaListener(topics = "${spring.kafka.post-events-topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePostEvent(String message) {
        log.info("Received post event from Kafka: {}", message);
        Post post = mapper.jsonToPost(message);
        if(post != null) {
            sendPostToAllFollowers(post);
        }
    }

    private void sendPostToAllFollowers(Post post) {
        Optional.ofNullable(followerService.getFollowers(post.username())).ifPresent(
                followers -> {
                    followers.forEach(username -> notificationEventProducer.sendNotificationEvent(
                            username,
                            "Hi " + username + ", there is new post from " + post.username() + "with content "
                            + post.username()
                    ));
                }
        );
    }
}
