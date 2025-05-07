package com.chatterbox.followerservice.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Service layer responsible for interacting with Redis to store follower/following data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisStorageService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addFollower(String userId, String followerUsername) {
        redisTemplate.opsForSet().add("followers:" + userId, followerUsername);
    }

    public void removeFollower(String userId, String followerUsername) {
        redisTemplate.opsForSet().remove("followers:" + userId, followerUsername);
    }

    public Set<String> getFollowers(String userId) {
        return redisTemplate.opsForSet().members("followers:" + userId);
    }

    public void addFollowing(String userId, String followeeUsername) {
        redisTemplate.opsForSet().add("following:" + userId, followeeUsername);
    }

    public void removeFollowing(String userId, String followeeUsername) {
        redisTemplate.opsForSet().remove("following:" + userId, followeeUsername);
    }

    public Set<String> getFollowing(String userId) {
        return redisTemplate.opsForSet().members("following:" + userId);
    }

    // Dev-only debugging method
    public Map<String, Set<String>> getAllData() {
        Map<String, Set<String>> allData = new HashMap<>();
        Set<String> keys = redisTemplate.keys("*");
        if (keys.isEmpty()) {
            log.info("No keys found in Follower redis database.");
            return null;
        }
        keys.forEach(key -> {
            Set<String> members = redisTemplate.opsForSet().members(key);
            log.info("Key: {}, Members: {}", key, members);
            allData.put(key, members);
        });
        return allData;
    }

    // debut / admin only
    public void deleteAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}
