package com.spring.twitterconsumer.payload;

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

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }
}
