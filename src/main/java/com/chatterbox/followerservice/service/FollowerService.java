package com.chatterbox.followerservice.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FollowerService {

    @Autowired
    private RedisStorageService storage;

    public void followUser(String followerId, String followeeId) {
        storage.follow(followerId,followeeId);

    }

    public void unfollowUser(String followerId, String followeeId) {
        storage.unfollow(followerId,followeeId);
    }

    public List<String> getFollowers(String userId) {
        return storage.getFollowers(userId).stream().toList();
        //return new ArrayList<>(followersMap.getOrDefault(userId, getDummyFollowers()));
    }

    public List<String> getFollowing(String userId) {
        return storage.getFollowing(userId).stream().toList();
        //return new ArrayList<>(followingMap.getOrDefault(userId, getDummyFollowing()));
    }

    private Set<String> getDummyFollowers() {
        return new HashSet<>(Arrays.asList("follower1", "follower2", "follower3"));
    }

    private Set<String> getDummyFollowing() {
        return new HashSet<>(Arrays.asList("following1", "following2", "following3"));
    }

    public void getAllRedisValues()
    {
        storage.printAllKeysAndValues();
    }

}
