package com.chatterbox.followerservice.controller;

import com.chatterbox.followerservice.model.FollowRequest;
import com.chatterbox.followerservice.service.FollowerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/follower")
@NoArgsConstructor
@AllArgsConstructor
public class FollowerController {

    @Autowired private FollowerService followerService;

    @PostMapping("/follow")
    public String followUser(@RequestBody FollowRequest request) {
        followerService.followUser(request.getFollowerId(), request.getFolloweeId());
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
}