package com.chatterbox.followerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
public class RedisStorageService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public RedisStorageService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String followingKey(String userId) {
        System.out.println("user:" + userId + ":following");
        return "user:" + userId + ":following";
    }

    private String followersKey(String userId) {
        System.out.println("user:" + userId + ":followers");
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
        System.out.println("redisobject=" + redisTemplate.opsForSet().toString());
        return redisTemplate.opsForSet().members(followersKey(userId));
    }

    public Set<String> getFollowing(String userId) {
        return redisTemplate.opsForSet().members(followingKey(userId));
    }


    public void printAllKeysAndValues() {
        Set<String> keys = redisTemplate.keys("*");

        if (keys == null || keys.isEmpty()) {
            System.out.println("No keys found in Redis.");
            return;
        }

        for (String key : keys) {
            DataType type = redisTemplate.type(key);

            System.out.println("Key: " + key);
            Set<String> members = redisTemplate.opsForSet().members(key);
            System.out.println("  Members: " + members);
        }
    }

}
