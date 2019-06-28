package com.spring.twitterapp.Payload;

/**
 * Created by ritakuo on 6/19/19.
 */
import com.spring.twitterapp.model.FollowRelation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Long tweetCount;

    public UserProfile(Long id, String username, String name, Instant joinedAt, Long tweetCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.tweetCount = tweetCount;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }


    public Long getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(Long tweetCount) {
        this.tweetCount = tweetCount;
    }
}