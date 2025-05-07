package com.chatterbox.followerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRedisService redisStorageService;

    public void followUser(String followerId, String followeeId) {
        redisStorageService.follow(followerId, followeeId);
    }

    public void unfollowUser(String followerId, String followeeId) {
        redisStorageService.unfollow(followerId, followeeId);
    }

    public List<String> getFollowers(String userId) {
        return new ArrayList<>(redisStorageService.getFollowers(userId));
    }

    public List<String> getFollowing(String userId) {
        return new ArrayList<>(redisStorageService.getFollowing(userId));
    }

    public Map<String, Set<String>> getAllRedisValues() {
        return redisStorageService.getAllData();
    }
}
