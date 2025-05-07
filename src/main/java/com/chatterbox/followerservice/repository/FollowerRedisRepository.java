package com.chatterbox.followerservice.repository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Log4j2
public class FollowerRedisRepository {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String followingKey(String userId) {
        log.info("user:{}:following", userId);
        return "user:" + userId + ":following";
    }

    private String followersKey(String userId) {
        log.info("user:{}:followers", userId);
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
        log.info("redis object={}", redisTemplate.opsForSet().toString());
        return redisTemplate.opsForSet().members(followersKey(userId));
    }

    public Set<String> getFollowing(String userId) {
        return redisTemplate.opsForSet().members(followingKey(userId));
    }


    public void printAllKeysAndValues() {
        Set<String> keys = redisTemplate.keys("*");

        if (keys == null || keys.isEmpty()) {
            log.info("No keys found in Redis.");
            return;
        }

        for (String key : keys) {
            DataType type = redisTemplate.type(key);

            log.info("Key: {}", key);
            Set<String> members = redisTemplate.opsForSet().members(key);
            log.info("  Members: {}", members);
        }
    }

}
