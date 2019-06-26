package com.spring.twitterapp.controller;

import com.spring.twitterapp.Payload.ApiResponse;
import com.spring.twitterapp.Payload.PagedResponse;
import com.spring.twitterapp.Payload.TweetRequest;
import com.spring.twitterapp.Payload.TweetResponse;
import com.spring.twitterapp.model.Tweet;
import com.spring.twitterapp.repository.TweetRepository;
import com.spring.twitterapp.security.CurrentUser;
import com.spring.twitterapp.security.UserPrincipal;
import com.spring.twitterapp.service.TweetService;
import com.spring.twitterapp.util.AppConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.tomcat.jni.Poll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by ritakuo on 6/19/19.
 */
@RestController
@RequestMapping("/api/tweets")
public class TweetController {
    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    TweetService tweetService;

    private static final Logger logger = LoggerFactory.getLogger(TweetController.class);


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTweet(@Valid @RequestBody TweetRequest tweetRequest) {
        Tweet tweet = tweetService.createTweet(tweetRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{tweetId}")
                .buildAndExpand(tweet.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Tweet Created Successfully"));
    }

    @GetMapping("/{tweetId}")
    public TweetResponse getTweetById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long tweetId) {
        return tweetService.getTweetById(tweetId, currentUser);
    }
    @GetMapping("/_all")
    public PagedResponse<TweetResponse>  getAllTweets(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return tweetService.getAllTweets(page, size);
    }
    @GetMapping("/following")
    public PagedResponse<TweetResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                                 @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return tweetService.getTweetsFromFollowing(currentUser, page, size);
    }




}
