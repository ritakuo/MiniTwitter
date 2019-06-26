package com.spring.twitterapp.Payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Created by ritakuo on 6/19/19.
 */
public class SignUpRequest {
    @NotEmpty
    @Size(min = 2, max = 40)
    private String name;

    @NotEmpty
    @Size(min=2, max=15)
    private String username;

    @NotEmpty
    @Size(max = 40)
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, max = 20)
    private String password;

    public String getusername() {
        return username;
    }
    public void setusername(String username) {
        this.username = username;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
