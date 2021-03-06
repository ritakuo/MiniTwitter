package com.spring.twitterapp.security;

import com.spring.twitterapp.exception.ResourceNotFoundException;
import com.spring.twitterapp.model.User;
import com.spring.twitterapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


/**
 To authenticate a User or perform various role-based checks, Spring security needs to load users details somehow.

 For this purpose, It consists of an interface called UserDetailsService which has a single method that loads a user based on username-

 UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

 this method provides implmentation for loadUserByUsername()
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    //used by spring security
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Let people login with either username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username : " + username)
                );

        return UserPrincipal.create(user);
    }
    //used by JWTAuthenticationFilter

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}