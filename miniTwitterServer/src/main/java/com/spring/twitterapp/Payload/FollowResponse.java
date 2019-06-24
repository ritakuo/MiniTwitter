package com.spring.twitterapp.Payload;

import java.time.Instant;

/**
 * Created by ritakuo on 6/21/19.
 */
public class FollowResponse {
    private String username_to_follow;
    private Instant followedDateTime;

    public FollowResponse(String username_to_follow, Instant followedDateTime) {
        this.username_to_follow = username_to_follow;
        this.followedDateTime = followedDateTime;
    }

    public String getUsername_to_follow() {
        return username_to_follow;
    }

    public void setUsername_to_follow(String username_to_follow) {
        this.username_to_follow = username_to_follow;
    }

    public Instant getFollowedDateTime() {
        return followedDateTime;
    }

    public void setFollowedDateTime(Instant followedDateTime) {
        this.followedDateTime = followedDateTime;
    }
}