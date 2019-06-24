package com.spring.twitterapp.util;

import com.spring.twitterapp.Payload.TweetResponse;
import com.spring.twitterapp.Payload.UserSummary;
import com.spring.twitterapp.model.Tweet;
import com.spring.twitterapp.model.User;

import java.time.Instant;

/**
 * Created by ritakuo on 6/19/19.
 */
public class ModelMapper {

    public static TweetResponse mapTweetToTweetResponse(Tweet tweet, User creator) {
        TweetResponse tweetResponse = new TweetResponse();
        tweetResponse.setId(tweet.getId());
        tweetResponse.setContent(tweet.getContent());
        tweetResponse.setCreationDateTime(tweet.getCreatedAt());

        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        tweetResponse.setCreatedBy(creatorSummary);


        return tweetResponse;
    }
}