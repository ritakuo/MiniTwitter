package com.spring.twitterapp.Payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by ritakuo on 6/21/19.
 */
public class FollowRequest {
    @NotBlank
    @Size(max = 140)
    private String username_to_follow;


    public String getUsername_to_follow() {
        return username_to_follow;
    }

    public void setUsername_to_follow(String username_to_follow) {
        this.username_to_follow = username_to_follow;
    }
}
