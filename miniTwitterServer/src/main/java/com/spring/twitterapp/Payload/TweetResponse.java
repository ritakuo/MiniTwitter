package com.spring.twitterapp.Payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Created by ritakuo on 6/19/19.
 */
public class TweetResponse {
    private Long id;
    private String content;
    private String createdBy;

//    private UserSummary createdBy;
    private Instant creationDateTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    //    public UserSummary getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(UserSummary createdBy) {
//        this.createdBy = createdBy;
//    }


    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }




}
