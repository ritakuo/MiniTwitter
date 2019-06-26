package com.spring.twitterconsumer.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FollowRequest {

    private String username_to_follow;

    public FollowRequest(String username_to_follow) {
        this.username_to_follow = username_to_follow;
    }

    public String getUsername_to_follow() {
        return username_to_follow;
    }

    public void setUsername_to_follow(String username_to_follow) {
        this.username_to_follow = username_to_follow;
    }
}
