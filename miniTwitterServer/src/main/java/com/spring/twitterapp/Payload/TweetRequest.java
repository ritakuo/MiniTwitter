package com.spring.twitterapp.Payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by ritakuo on 6/19/19.
 */
public class TweetRequest {
    @NotBlank
    @Size(max = 140)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
