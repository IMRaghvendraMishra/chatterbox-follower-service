package com.chatterbox.followerservice.controller;

import com.chatterbox.followerservice.kafka.FollowEventProducer;
import com.chatterbox.followerservice.model.FollowRequest;
import com.chatterbox.followerservice.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/follower")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerService followerService;
    private final FollowEventProducer followEventProducer;

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestBody FollowRequest request) {
        followerService.followUser(request.getFollowerId(), request.getFolloweeId());
        followEventProducer.sendPostCreatedEvent(request);
        return ResponseEntity.ok("Followed successfully");
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestBody FollowRequest request) {
        followerService.unfollowUser(request.getFollowerId(), request.getFolloweeId());
        return ResponseEntity.ok("Unfollowed successfully");
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<String>> getFollowers(@PathVariable String userId) {
        return ResponseEntity.ok(followerService.getFollowers(userId));
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<String>> getFollowing(@PathVariable String userId) {
        return ResponseEntity.ok(followerService.getFollowing(userId));
    }

    @GetMapping("/debug/keys")
    public ResponseEntity<Map<String, Set<String>>> getAllRedisValues() {
        return ResponseEntity.ok(followerService.getAllRedisValues());
    }
}