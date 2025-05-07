package com.chatterbox.followerservice.controller;

import com.chatterbox.followerservice.dto.FollowRequest;
import com.chatterbox.followerservice.service.FollowerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowerController.class)
class FollowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowerService followerService;

    @Mock
    private FollowRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFollow() throws Exception {
        when(request.getFollowerUsername()).thenReturn("alice");
        when(request.getFolloweeUsername()).thenReturn("bob");

        mockMvc.perform(post("/api/v1/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Follow successful"));

        verify(followerService).follow("alice", "bob");
    }

    @Test
    void testUnfollow() throws Exception {
        when(request.getFollowerUsername()).thenReturn("alice");
        when(request.getFolloweeUsername()).thenReturn("bob");

        mockMvc.perform(post("/api/v1/follow/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Unfollow successful"));

        verify(followerService).unfollow("alice", "bob");
    }

    @Test
    void testGetFollowers() throws Exception {
        Set<String> followers = Set.of("user1", "user2");
        when(followerService.getFollowers("bob")).thenReturn(followers);

        mockMvc.perform(get("/api/v1/follow/followers/bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("user1"))
                .andExpect(jsonPath("$[1]").value("user2"));
    }

    @Test
    void testGetFollowing() throws Exception {
        Set<String> following = Set.of("userX", "userY");
        when(followerService.getFollowing("alice")).thenReturn(following);

        mockMvc.perform(get("/api/v1/follow/following/alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("userX"))
                .andExpect(jsonPath("$[1]").value("userY"));
    }

    @Test
    void testGetAllRedisValues() throws Exception {
        Map<String, Set<String>> redisData = Map.of(
                "followers:alice", Set.of("bob", "carol"),
                "following:bob", Set.of("dave")
        );

        when(followerService.getAllRedisValues()).thenReturn(redisData);

        mockMvc.perform(get("/api/v1/follow/debug/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['followers:alice']").isArray())
                .andExpect(jsonPath("$.['followers:alice']").isNotEmpty());
    }

    @Test
    void testDeleteAll() throws Exception {
        when(followerService.deleteAll()).thenReturn("All records deleted");

        mockMvc.perform(delete("/api/v1/follow/debug/deleteAll"))
                .andExpect(status().isOk())
                .andExpect(content().string("All records deleted"));
    }
}
