package com.chatterbox.followerservice.controller;

import com.chatterbox.followerservice.dto.FollowRequest;
import com.chatterbox.followerservice.service.FollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * REST controller exposing endpoints to follow and unfollow users,
 * and retrieve follower/following relationships.
 */
@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping
    public ResponseEntity<String> follow(@RequestBody @Valid FollowRequest request) {
        log.info("Follow request: {}", request);
        followerService.follow(request.getFollowerUsername(), request.getFolloweeUsername());
        return ResponseEntity.ok("Follow successful");
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestBody @Valid FollowRequest request) {
        log.info("Unfollow request: {}", request);
        followerService.unfollow(request.getFollowerUsername(), request.getFolloweeUsername());
        return ResponseEntity.ok("Unfollow successful");
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<Set<String>> getFollowers(@PathVariable String userId) {
        return ResponseEntity.ok(followerService.getFollowers(userId));
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<Set<String>> getFollowing(@PathVariable String userId) {
        return ResponseEntity.ok(followerService.getFollowing(userId));
    }

    @GetMapping("/debug/getAll")
    public ResponseEntity<Map<String, Set<String>>> getAllRedisValues() {
        Map<String, Set<String>> allRecords = followerService.getAllRedisValues();
        return ResponseEntity.ok(allRecords != null ? allRecords : new HashMap<>());
    }

    @DeleteMapping("/debug/deleteAll")
    public ResponseEntity<String> deleteAll() {
        return ResponseEntity.ok(followerService.deleteAll());
    }
}