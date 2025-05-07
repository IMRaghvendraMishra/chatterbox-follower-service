package com.chatterbox.followerservice.service;

import com.chatterbox.followerservice.repository.FollowerRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRedisRepository redisStorageService;

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

    public void getAllRedisValues() {
        redisStorageService.printAllKeysAndValues();
    }
}
