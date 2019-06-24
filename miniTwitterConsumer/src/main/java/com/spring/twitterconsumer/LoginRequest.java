package com.spring.twitterconsumer;

import javax.validation.constraints.NotEmpty;

public class LoginRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    public LoginRequest(@NotEmpty String username, @NotEmpty String password) {
        this.username = username;
        this.password = password;
    }

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
