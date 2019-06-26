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
