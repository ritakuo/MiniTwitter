package com.spring.twitterapp.controller;

import com.spring.twitterapp.Payload.*;
import com.spring.twitterapp.exception.ResourceNotFoundException;
import com.spring.twitterapp.model.FollowRelation;
import com.spring.twitterapp.model.User;
import com.spring.twitterapp.repository.FollowRepository;
import com.spring.twitterapp.repository.TweetRepository;
import com.spring.twitterapp.repository.UserRepository;
import com.spring.twitterapp.security.CurrentUser;
import com.spring.twitterapp.security.UserPrincipal;
import com.spring.twitterapp.service.TweetService;
import com.spring.twitterapp.service.UserService;
import com.spring.twitterapp.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ritakuo on 6/20/19.
 */
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowRepository followRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/users/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long tweetCount = tweetRepository.countByCreatedBy(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), tweetCount);

        return userProfile;
    }
    @GetMapping("/users/{username}/tweets")
    public PagedResponse<TweetResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                          @CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return tweetService.getTweetCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/users/followers")
    @PreAuthorize("hasRole('USER')")
    public List<UserSummary>  getUserFollowers(@CurrentUser UserPrincipal currentUser,
                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getName()));


        List<UserSummary> rtnList = new ArrayList<>();

        for(FollowRelation follower: followRepository.findByTo(user)){
                User oneFollower = follower.getFrom();
                UserSummary userSummary = new UserSummary(oneFollower.getId(), oneFollower.getUsername(), oneFollower.getName());
                rtnList.add(userSummary);
        }

        return rtnList;

    }

    @PostMapping("/users/follow")
    @PreAuthorize("hasRole('USER')")
    public FollowResponse followingUser(
                                    @CurrentUser UserPrincipal currentUser,
                                    @Valid @RequestBody FollowRequest folowRequest,
                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        User curUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getName()));
        return userService.follow(curUser, folowRequest);

    }


}
