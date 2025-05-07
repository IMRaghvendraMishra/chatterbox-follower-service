package com.chatterbox.followerservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for carrying follow/unfollow request payloads.
 */
@Data
public class FollowRequest {

    @NotBlank(message = "followerUsername is required")
    private String followerUsername;

    @NotBlank(message = "followeeUsername is required")
    private String followeeUsername;
}
