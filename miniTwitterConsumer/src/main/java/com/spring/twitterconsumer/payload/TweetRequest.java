package com.spring.twitterconsumer.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by ritakuo on 6/22/19.
 */
public class TweetRequest {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

