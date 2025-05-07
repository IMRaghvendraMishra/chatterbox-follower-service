package com.chatterbox.followerservice.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisStorageServiceTest {

    private RedisTemplate<String, String> redisTemplate;
    private SetOperations<String, String> setOps;
    private RedisStorageService storageService;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        setOps = mock(SetOperations.class);
        when(redisTemplate.opsForSet()).thenReturn(setOps);

        storageService = new RedisStorageService(redisTemplate);
    }

    @Test
    void shouldAddFollowerToSet() {
        storageService.addFollower("user123", "alice");
        verify(setOps).add("followers:user123", "alice");
    }

    @Test
    void shouldRemoveFollowerFromSet() {
        storageService.removeFollower("user123", "alice");
        verify(setOps).remove("followers:user123", "alice");
    }

    @Test
    void shouldAddFolloweeToFollowingSet() {
        storageService.addFollowing("user123", "bob");
        verify(setOps).add("following:user123", "bob");
    }

    @Test
    void shouldRemoveFolloweeFromFollowingSet() {
        storageService.removeFollowing("user123", "bob");
        verify(setOps).remove("following:user123", "bob");
    }

    @Test
    void shouldReturnFollowersFromRedis() {
        Set<String> expected = Set.of("f1", "f2");
        when(setOps.members("followers:user123")).thenReturn(expected);

        Set<String> result = storageService.getFollowers("user123");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnFolloweesFromRedis() {
        Set<String> expected = Set.of("a", "b");
        when(setOps.members("following:user123")).thenReturn(expected);

        Set<String> result = storageService.getFollowing("user123");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldGetAllDataFromRedis() {
        Set<String> keys = Set.of("followers:user1", "following:user2");
        Set<String> val1 = Set.of("a", "b");
        Set<String> val2 = Set.of("x");

        when(redisTemplate.keys("*")).thenReturn(keys);
        when(setOps.members("followers:user1")).thenReturn(val1);
        when(setOps.members("following:user2")).thenReturn(val2);

        Map<String, Set<String>> result = storageService.getAllData();

        assertThat(result).hasSize(2);
        assertThat(result.get("followers:user1")).containsExactlyInAnyOrder("a", "b");
        assertThat(result.get("following:user2")).containsExactly("x");
    }

    @Test
    void shouldReturnNullIfNoKeysExist() {
        when(redisTemplate.keys("*")).thenReturn(Set.of());

        Map<String, Set<String>> result = storageService.getAllData();

        assertThat(result).isNull();
    }

    @Test
    void shouldDeleteAllKeys() {
        Set<String> keys = Set.of("followers:user1", "following:user2");
        when(redisTemplate.keys("*")).thenReturn(keys);

        storageService.deleteAll();

        verify(redisTemplate).delete(keys);
    }
}
