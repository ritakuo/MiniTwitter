package com.spring.twitterapp.service;

import com.spring.twitterapp.Payload.PagedResponse;
import com.spring.twitterapp.Payload.TweetRequest;
import com.spring.twitterapp.Payload.TweetResponse;
import com.spring.twitterapp.Payload.UserSummary;
import com.spring.twitterapp.exception.BadRequestException;
import com.spring.twitterapp.exception.ResourceNotFoundException;
import com.spring.twitterapp.model.FollowRelation;
import com.spring.twitterapp.model.Tweet;
import com.spring.twitterapp.model.User;
import com.spring.twitterapp.repository.FollowRepository;
import com.spring.twitterapp.repository.TweetRepository;
import com.spring.twitterapp.repository.UserRepository;
import com.spring.twitterapp.security.UserPrincipal;
import com.spring.twitterapp.util.AppConstants;
import com.spring.twitterapp.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ritakuo on 6/19/19.
 */
@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    private static final Logger logger = LoggerFactory.getLogger(TweetService.class);

    public PagedResponse<TweetResponse> getAllTweets(int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Tweets
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Tweet> tweets = tweetRepository.findAll(pageable);

        if(tweets.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), tweets.getNumber(),
                    tweets.getSize(), tweets.getTotalElements(), tweets.getTotalPages(), tweets.isLast());
        }

        Map<Long, User> creatorMap = getTweetCreatorMap(tweets.getContent());

        List<TweetResponse> tweetResponses = tweets.map(tweet -> {
            return ModelMapper.mapTweetToTweetResponse(tweet,
                    creatorMap.get(tweet.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(tweetResponses, tweets.getNumber(),
                tweets.getSize(), tweets.getTotalElements(), tweets.getTotalPages(), tweets.isLast());
    }

    public PagedResponse<TweetResponse> getTweetsFromFollowing(UserPrincipal currentUser, int page, int size){
        validatePageNumberAndSize(page, size);

        //get all the following
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getName()));
        List<Long> followings_ids = new ArrayList<>();
        for(FollowRelation following: followRepository.findByFrom(user)){
            User oneFollowing = following.getTo();
            followings_ids.add(oneFollowing.getId());
        }

        // Retrieve Tweets
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Tweet> tweets = tweetRepository.findByCreatedByIn(followings_ids, pageable);

        Map<Long, User> creatorMap = getTweetCreatorMap(tweets.getContent());

        List<TweetResponse> tweetResponses = tweets.map(tweet -> {
            return ModelMapper.mapTweetToTweetResponse(tweet,
                    creatorMap.get(tweet.getCreatedBy()));
        }).getContent();
        return new PagedResponse<>(tweetResponses, tweets.getNumber(),
                tweets.getSize(), tweets.getTotalElements(), tweets.getTotalPages(), tweets.isLast());


    }
    public PagedResponse<TweetResponse> getTweetCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all tweets created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Tweet> tweets = tweetRepository.findByCreatedBy(user.getId(), pageable);

        if (tweets.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), tweets.getNumber(),
                    tweets.getSize(), tweets.getTotalElements(), tweets.getTotalPages(), tweets.isLast());
        }

        List<TweetResponse> tweetResponses = tweets.map(tweet -> {
            return ModelMapper.mapTweetToTweetResponse(tweet,
                    user);
        }).getContent();

        return new PagedResponse<>(tweetResponses, tweets.getNumber(),
                tweets.getSize(), tweets.getTotalElements(), tweets.getTotalPages(), tweets.isLast());
    }
    public Tweet createTweet(TweetRequest tweetRequest) {
        Tweet tweet = new Tweet();
        tweet.setContent(tweetRequest.getContent());
        return tweetRepository.save(tweet);
    }
    public TweetResponse getTweetById(Long tweetId, UserPrincipal currentUser) {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(
                () -> new ResourceNotFoundException("Tweet", "id", tweetId));


        // Retrieve tweet creator details
        User creator = userRepository.findById(tweet.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", tweet.getCreatedBy()));

        return ModelMapper.mapTweetToTweetResponse(tweet, creator);
    }
    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
    Map<Long, User> getTweetCreatorMap(List<Tweet> tweets) {
        // Get Tweet Creator details of the given list of polls
        List<Long> creatorIds = tweets.stream()
                .map(Tweet::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }

}
