package com.spring.twitterconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.twitterconsumer.payload.FollowRequest;
import com.spring.twitterconsumer.payload.SignInAndFollowRequest;
import com.spring.twitterconsumer.payload.LoginRequest;
import com.spring.twitterconsumer.payload.SignUpRequest;
//import com.spring.twitterconsumer.security.UserPrincipal;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by ritakuo on 6/22/19.
 */
@RestController
public class ConsumerWebService {
    @Autowired
    RestTemplate restTemplate;

//    @Value("${remoteServerUrl}")
//    private String remoteServerURL;
//
//    @Value("${remoteServerPort}")
//    private String remoteServerPort;

    private String remoteServerbaseURL="http://localhost:8080";
    //private String remoteServerbaseURL="http://demoserver.kuorita.com";

    private static final Logger logger = LoggerFactory.getLogger(ConsumerWebService.class);


    @PostMapping(value = "/signin")
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
        System.out.println(response.getBody());
        return response;
    }

    @PostMapping(value = "/signup")
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

    //sets the context that User A (Logged in User) is following User B.
    @PutMapping(value = "/follow")
    public ResponseEntity<String> signInAndFollow(@Valid @RequestBody FollowRequest followRequest,
                                                  @RequestHeader(value="Authorization") String authorizationHeader) {
        String url = remoteServerbaseURL +"/api/users/follow";

        ObjectMapper mapper = new ObjectMapper();

        String requestJson="";
        try{
            requestJson = mapper.writeValueAsString(followRequest);
        }catch(Exception e){
            logger.error(e.toString());
        }
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, createHeadersWithToken(authorizationHeader));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }

    //To list all the followers for the logged in user
    @GetMapping(value = "/followers")
    public String signInAndGetFollowers(@RequestHeader(value="Authorization") String authorizationHeader) {
        String url = remoteServerbaseURL +"/api/users/followers";

        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(authorizationHeader));

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
    }

    //To list 100 recent tweets for the logged in user.
    @GetMapping(value = "/feed")
    public String signInAndGetFeed(@RequestHeader(value="Authorization") String authorizationHeader) {
        String url = remoteServerbaseURL +"/api/tweets/following";
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithToken(authorizationHeader));

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class).getBody();
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
        String token = response.getBody().split(",")[0].split(":")[1].replaceAll("^\"|\"$", "");
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


    @GetMapping(value = "/users/{username}/tweet")
    public String getUserTweetList(@PathVariable("username") String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        return restTemplate.exchange(
                remoteServerbaseURL +"/api/users/"+ username+"/tweets", HttpMethod.GET, entity, String.class).getBody();
    }

    //not used
    HttpHeaders createHeadersWithJWT (String username, String password){
        return new HttpHeaders() {{
            //get the authorization bearer response
            setContentType(MediaType.APPLICATION_JSON);
            String token = getJWTToken(username, password);

            set( "Authorization", "Bearer "+ token);
        }};
    }


    //    //get news feed for that user with login request
//    @PostMapping(value = "/feed")
//    public String signInAndGetFeed(@Valid @RequestBody LoginRequest loginRequest) {
//        String url = remoteServerbaseURL +"/api/tweets/following";
//        String username = loginRequest.getusername();
//        String password = loginRequest.getpassword();
//
//        HttpEntity<String> entity = new HttpEntity<String>(createHeadersWithJWT(username,password));
//        return restTemplate.exchange(
//                url, HttpMethod.GET, entity, String.class).getBody();
//    }

    //https://stackoverflow.com/questions/21101250/sending-get-request-with-authentication-headers-using-resttemplate
    //https://stackoverflow.com/questions/4615039/spring-security-authentication-using-resttemplate
    //REading HTTP request header http://appsdeveloperblog.com/read-http-request-header-in-spring-mvc/


}
