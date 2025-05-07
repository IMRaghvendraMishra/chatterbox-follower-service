package com.chatterbox.followerservice.service;

import com.chatterbox.followerservice.messaging.FollowerEventProducer;
import com.chatterbox.followerservice.storage.RedisStorageService;
import com.chatterbox.followerservice.util.ObjectJsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FollowerServiceTest {

    private RedisStorageService redisStorageService;
    private FollowerEventProducer eventProducer;
    private ObjectJsonMapper mapper;
    private FollowerService followerService;

    @BeforeEach
    void setUp() {
        redisStorageService = mock(RedisStorageService.class);
        eventProducer = mock(FollowerEventProducer.class);
        mapper = mock(ObjectJsonMapper.class);
        followerService = new FollowerService(redisStorageService, eventProducer, mapper);
    }

    @Test
    void shouldFollowUser() {
        String follower = "alice";
        String followee = "bob";

        followerService.follow(follower, followee);

        verify(redisStorageService).addFollower(followee, follower);
        verify(redisStorageService).addFollowing(follower, followee);
        verify(eventProducer).sendFollowEvent(follower, followee);
    }

    @Test
    void shouldUnfollowUser() {
        String follower = "alice";
        String followee = "bob";

        followerService.unfollow(follower, followee);

        verify(redisStorageService).removeFollower(followee, follower);
        verify(redisStorageService).removeFollowing(follower, followee);
        verifyNoInteractions(eventProducer); // event is not sent for unfollow
    }

    @Test
    void shouldReturnFollowers() {
        Set<String> followers = Set.of("x", "y");
        when(redisStorageService.getFollowers("bob")).thenReturn(followers);

        Set<String> result = followerService.getFollowers("bob");

        assertThat(result).containsExactlyInAnyOrder("x", "y");
        verify(redisStorageService).getFollowers("bob");
    }

    @Test
    void shouldReturnFollowing() {
        Set<String> following = Set.of("a", "b");
        when(redisStorageService.getFollowing("alice")).thenReturn(following);

        Set<String> result = followerService.getFollowing("alice");

        assertThat(result).containsExactlyInAnyOrder("a", "b");
        verify(redisStorageService).getFollowing("alice");
    }

    @Test
    void shouldReturnAllRedisValues() {
        Map<String, Set<String>> mockData = Map.of("followers:alice", Set.of("bob"));
        when(redisStorageService.getAllData()).thenReturn(mockData);

        Map<String, Set<String>> result = followerService.getAllRedisValues();

        assertThat(result).isEqualTo(mockData);
        verify(redisStorageService).getAllData();
    }

    @Test
    void shouldDeleteAllAndReturnMessage() {
        String result = followerService.deleteAll();

        assertThat(result).isEqualTo("All follower and followee records deleted");
        verify(redisStorageService).deleteAll();
    }
}
