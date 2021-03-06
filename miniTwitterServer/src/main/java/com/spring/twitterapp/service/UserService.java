package com.spring.twitterapp.service;

import com.spring.twitterapp.Payload.FollowRequest;
import com.spring.twitterapp.Payload.FollowResponse;
import com.spring.twitterapp.exception.ResourceNotFoundException;
import com.spring.twitterapp.model.FollowRelation;
import com.spring.twitterapp.model.User;
import com.spring.twitterapp.repository.FollowRepository;
import com.spring.twitterapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

/**
 * Created by ritakuo on 6/21/19.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public FollowResponse follow(User curUser, FollowRequest followRequest) {

        User followedUser = userRepository.findByUsername(followRequest.getUsername_to_follow())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", followRequest.getUsername_to_follow()));
        FollowRelation follow_relation = new FollowRelation(curUser, followedUser);
        followRepository.save(follow_relation);

        return new FollowResponse(followedUser.getName(), now());
    }

    public List<Object[]> getAllUsers(int page, int size){

        // Retrieve Tweets
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        Page<User> users = userRepository.findAll(pageable);
        List<Object[]> rtn = new ArrayList<>();
        for(User user: users){
            rtn.add(new Object[]{user.getId(), user.getUsername()});
        }


        return rtn;

    }
    public User retrieveUserById(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        return user;
    }



}
