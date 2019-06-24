package com.spring.twitterapp.Payload;

import javax.validation.constraints.NotEmpty;

/**
 * Created by ritakuo on 6/19/19.
 */
public class LoginRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    public String getUserName() {
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
}
