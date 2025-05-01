package com.chatterbox.followerservice.service;

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

    private final Map<String, Set<String>> followersMap = new HashMap<>();
    private final Map<String, Set<String>> followingMap = new HashMap<>();

    public void followUser(String followerId, String followeeId) {
        followersMap.computeIfAbsent(followeeId, k -> getDummyFollowers()).add(followerId);
        followingMap.computeIfAbsent(followerId, k -> getDummyFollowing()).add(followeeId);
    }

    public void unfollowUser(String followerId, String followeeId) {
        followersMap.getOrDefault(followeeId, getDummyFollowers()).remove(followerId);
        followingMap.getOrDefault(followerId, getDummyFollowing()).remove(followeeId);
    }

    public List<String> getFollowers(String userId) {
        return new ArrayList<>(followersMap.getOrDefault(userId, getDummyFollowers()));
    }

    public List<String> getFollowing(String userId) {
        return new ArrayList<>(followingMap.getOrDefault(userId, getDummyFollowing()));
    }

    private Set<String> getDummyFollowers() {
        return new HashSet<>(Arrays.asList("follower1", "follower2", "follower3"));
    }

    private Set<String> getDummyFollowing() {
        return new HashSet<>(Arrays.asList("following1", "following2", "following3"));
    }
}
