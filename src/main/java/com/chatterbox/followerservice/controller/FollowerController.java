package com.chatterbox.followerservice.controller;

import com.chatterbox.followerservice.kafka.FollowEventProducer;
import com.chatterbox.followerservice.model.FollowRequest;
import com.chatterbox.followerservice.service.FollowerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follower")
@NoArgsConstructor
@AllArgsConstructor
public class FollowerController {

    @Autowired private FollowerService followerService;
    @Autowired private FollowEventProducer followEventProducer;

    @PostMapping("/follow")
    public String followUser(@RequestBody FollowRequest request) {
        followerService.followUser(request.getFollowerId(), request.getFolloweeId());
        followEventProducer.sendPostCreatedEvent(request); // Send event to Kafka topic
        return "Followed successfully";
    }

    @PostMapping("/unfollow")
    public String unfollowUser(@RequestBody FollowRequest request) {
        followerService.unfollowUser(request.getFollowerId(), request.getFolloweeId());
        return "Unfollowed successfully";
    }

    @GetMapping("/followers/{userId}")
    public List<String> getFollowers(@PathVariable String userId) {
        return followerService.getFollowers(userId);
    }

    @GetMapping("/following/{userId}")
    public List<String> getFollowing(@PathVariable String userId) {
        return followerService.getFollowing(userId);
    }

    @GetMapping("/getAllKeys")
    public void getAllRedisValues()
    {
        followerService.getAllRedisValues();
    }

    // Catch-all fallback for invalid sub-paths
    @RequestMapping("**")
    public ResponseEntity<Map<String, Object>> handleInvalidPath() {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Invalid Endpoint");
        body.put("message", "The requested endpoint is not valid. Please check the URL.");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}