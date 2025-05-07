package com.chatterbox.followerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
@Log4j2
@RequiredArgsConstructor
public class FollowerRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private String followingKey(String userId) {
        return "user:" + userId + ":following";
    }

    private String followersKey(String userId) {
        return "user:" + userId + ":followers";
    }

    public void follow(String followerId, String followeeId) {
        redisTemplate.opsForSet().add(followingKey(followerId), followeeId);
        redisTemplate.opsForSet().add(followersKey(followeeId), followerId);
    }

    public void unfollow(String followerId, String followeeId) {
        redisTemplate.opsForSet().remove(followingKey(followerId), followeeId);
        redisTemplate.opsForSet().remove(followersKey(followeeId), followerId);
    }

    public Set<String> getFollowers(String userId) {
        return redisTemplate.opsForSet().members(followersKey(userId));
    }

    public Set<String> getFollowing(String userId) {
        return redisTemplate.opsForSet().members(followingKey(userId));
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
}
