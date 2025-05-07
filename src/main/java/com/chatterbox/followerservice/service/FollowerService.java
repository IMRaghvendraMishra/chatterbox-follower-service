package com.chatterbox.followerservice.service;

import com.chatterbox.followerservice.messaging.NotificationEventProducer;
import com.chatterbox.followerservice.storage.RedisStorageService;
import com.chatterbox.followerservice.util.ObjectJsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Core business service handling follower relationships.
 * Validates users, updates Redis sets, and publishes events to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FollowerService {

    private final RedisStorageService redisStorageService;
    private final NotificationEventProducer notificationEventProducer;
    private final ObjectJsonMapper objectJsonMapper;

    public void follow(String followerUsername, String followeeUsername) {
        redisStorageService.addFollower(followeeUsername, followerUsername);
        redisStorageService.addFollowing(followerUsername, followeeUsername);
        notificationEventProducer.sendNotificationEvent(followeeUsername,
                String.format("Hi %s, %s started following you", followeeUsername, followerUsername));
        log.info("User {} followed {}", followerUsername, followeeUsername);
    }

    public void unfollow(String followerUsername, String followeeUsername) {
        redisStorageService.removeFollower(followeeUsername, followerUsername);
        redisStorageService.removeFollowing(followerUsername, followeeUsername);
        log.info("User {} unfollowed {}", followerUsername, followeeUsername);
    }

    public Set<String> getFollowers(String userId) {
        return redisStorageService.getFollowers(userId);
    }

    public Set<String> getFollowing(String userId) {
        return redisStorageService.getFollowing(userId);
    }

    // debut / admin only
    public Map<String, Set<String>> getAllRedisValues() {
        return redisStorageService.getAllData();
    }

    // debut / admin only
    public String deleteAll() {
        redisStorageService.deleteAll();
        return "All follower and followee records deleted";
    }
}
