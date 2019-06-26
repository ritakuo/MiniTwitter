package com.spring.twitterconsumer.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignInAndFollowRequest {
    private String username;
    private String password;
    private String username_to_follow;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername_to_follow() {
        return username_to_follow;
    }

    public void setUsername_to_follow(String username_to_follow) {
        this.username_to_follow = username_to_follow;
    }
}
