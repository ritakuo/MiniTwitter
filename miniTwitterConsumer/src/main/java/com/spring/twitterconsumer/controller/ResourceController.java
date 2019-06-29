package com.spring.twitterconsumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.twitterconsumer.payload.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * Created by ritakuo on 6/22/19.
 */
@RestController
public class ResourceController {
    @Autowired
    RestTemplate restTemplate;


    //private String remoteServerbaseURL="http://localhost:8080";
    private String remoteServerbaseURL="http://demoserver.kuorita.com";

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @GetMapping("/")
    public String homePage() {
        return "Mini Twitter Consumer";
    }

    @PostMapping(value = "/signin")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully sign in"),
            @ApiResponse(code = 404, message = "User not found")})
    @ApiOperation(value = "Sign in to the service and get API token")
    public ResponseEntity<String> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        String url = remoteServerbaseURL +"/api/auth/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();

        String requestJson="";
        try{
            requestJson = mapper.writeValueAsString(loginRequest);
            System.out.println(requestJson);
        }catch(Exception e){
            System.out.println(e);
        }
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }

    @PostMapping(value = "/signup")
    @ApiOperation(value = "Signup to the service")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully sign up"),
            @ApiResponse(code = 400, message = "User name already taken")})
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        String url = remoteServerbaseURL +"/api/auth/signup";
        System.out.println(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();

        String requestJson="";
        try{
            requestJson = mapper.writeValueAsString(signUpRequest);
            System.out.println(requestJson);
        }catch(Exception e){
            System.out.println(e);
        }
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }


    @PutMapping(value = "/follow")
    @ApiOperation(value = "sets the context that User A (Logged in User) is following User B.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully follow"),
            @ApiResponse(code = 404, message = "User not found")})
    public String follow(@Valid @RequestBody FollowRequest followRequest,
                                                  @RequestHeader(value="username") String username,
                                                 @RequestHeader(value="password") String password) {
        String url = remoteServerbaseURL +"/api/users/follow";

        ObjectMapper mapper = new ObjectMapper();

        String requestJson="";
        try{
            requestJson = mapper.writeValueAsString(followRequest);
        }catch(Exception e){
            logger.error(e.toString());
        }
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, createHeadersWithToken(getJWTToken(username, password)));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response.getBody();
    }

    @GetMapping(value = "/followers")
    @ApiOperation(value = "List all the followers for the logged in user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully get followers")})
    public String getFollowers(@RequestHeader(value="username") String username,
                               @RequestHeader(value="password") String password) {
        String url = remoteServerbaseURL +"/api/users/followers";

        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(getJWTToken(username, password)));

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }
    @GetMapping(value = "/following")
    @ApiOperation(value = "List all the followers for the logged in user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully get followers")})
    public String getFollowings(@RequestHeader(value="username") String username,
                                @RequestHeader(value="password") String password) {
        String url = remoteServerbaseURL +"/api/users/following";

        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(getJWTToken(username, password)));

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }


    @GetMapping(value = "/feed")
    @ApiOperation(value = "List 100 recent feed from the users followed by the logged in user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully get feed")})
    public String getFeed(@RequestHeader(value="username") String username,
                          @RequestHeader(value="password") String password) {
        String url = remoteServerbaseURL +"/api/tweets/following";
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(getJWTToken(username, password)));

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }

    @PostMapping(value = "/feed")
    @ApiOperation(value = "Create a new tweet ")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully create new tweet")})
    public ResponseEntity<String> createTweet(@RequestHeader(value="username") String username,
                                              @RequestHeader(value="password") String password, @Valid @RequestBody TweetRequest tweetRequest) {

        String url = remoteServerbaseURL +"/api/tweets";

        ObjectMapper mapper = new ObjectMapper();

        String requestJson="";
        try{
            requestJson = mapper.writeValueAsString(tweetRequest);
        }catch(Exception e){
            logger.error(e.toString());
        }
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, createHeadersWithToken(getJWTToken(username, password)));
        System.out.println(entity);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }
    @DeleteMapping(value = "/feed/{tweetId}")
    @ApiOperation(value = "Delete logged in user's tweet by tweet ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully delete tweet"),
            @ApiResponse(code = 404, message = "tweet id not found"),
                    @ApiResponse(code = 405, message = "tweet id not belong to logged in user")
    })
    public ResponseEntity<?> deleteTweet(@RequestHeader(value="username") String username,
                                         @RequestHeader(value="password") String password,  @PathVariable Long tweetId) {
        String url = remoteServerbaseURL +"/api/tweets/" + tweetId;
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(getJWTToken(username, password)));
        return restTemplate.exchange(
                url, HttpMethod.DELETE, entity, String.class);
    }

    @GetMapping(value = "/feed/me")
    @ApiOperation(value = "Get logged in user's own tweets")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully get feed for logged in user")})
    public String getOwnTweets(@RequestHeader(value="username") String username,
                               @RequestHeader(value="password") String password) {
        String url = remoteServerbaseURL +"/api/tweets/me";
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(getJWTToken(username, password)));

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }
    @GetMapping(value = "/feed/_all")
    @ApiOperation(value = "Get All tweets")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully get all feed")})
    public String getAllTweets() {
        String url = remoteServerbaseURL +"/api/tweets/_all";
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithoutToken());

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }

    @GetMapping(value = "/users/me")
    @ApiOperation(value = "Get user profile from the login user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully get detail for the logged in user")})
    public String getOwnProfile(@RequestHeader(value="username") String username,
                                @RequestHeader(value="password") String password) {
        String url = remoteServerbaseURL +"/api/users/me";
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(getJWTToken(username, password)));

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }
    @ApiOperation(value = "Get user profile based on username")
    @GetMapping(value = "/users/{username}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully get detail for the user"),
            @ApiResponse(code = 404, message = "user not found")})
    public String getUserProfile(@PathVariable(value = "username") String username) {
        String url = remoteServerbaseURL +"/api/users/"+username;
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithoutToken());

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }

    //
    @GetMapping(value = "/users/{username}/tweet")
    public String getUserTweetList(@PathVariable("username") String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        return restTemplate.exchange(
                remoteServerbaseURL +"/api/users/"+ username+"/tweets", HttpMethod.GET, entity, String.class).getBody();
    }

    public String getJWTToken(String username, String password) {
        String url = remoteServerbaseURL +"/api/auth/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();

        String requestJson="";
        LoginRequest loginRequest = new LoginRequest(username, password);
        try{
            requestJson = mapper.writeValueAsString(loginRequest);
            System.out.println(requestJson);
        }catch(Exception e){
            logger.error(e.toString());
        }
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        String token = "Bearer "+ response.getBody().split(",")[0].split(":")[1].replaceAll("^\"|\"$", "");

        logger.info("token: " + token);
        return token;
    }

    HttpHeaders createHeadersWithToken (String token){
        return new HttpHeaders() {{
            //get the authorization bearer response
            setContentType(MediaType.APPLICATION_JSON);

            set( "Authorization", token);
        }};
    }
    HttpHeaders createHeadersWithoutToken (){
        return new HttpHeaders() {{
            //get the authorization bearer response
            setContentType(MediaType.APPLICATION_JSON);

        }};
    }
}
