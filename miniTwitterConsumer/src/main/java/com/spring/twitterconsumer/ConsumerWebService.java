package com.spring.twitterconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.twitterconsumer.payload.FollowRequest;
import com.spring.twitterconsumer.payload.SignInAndFollowRequest;
import com.spring.twitterconsumer.payload.LoginRequest;
import com.spring.twitterconsumer.payload.SignUpRequest;
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
//    private String remoteServerbaseURL="http://demoserver.kuorita.com";

    private static final Logger logger = LoggerFactory.getLogger(ConsumerWebService.class);
    

    @GetMapping(value = "/users/{username}/tweet")
    public String getUserTweetList(@PathVariable("username") String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        return restTemplate.exchange(
                remoteServerbaseURL +"/api/users/"+ username+"/tweets", HttpMethod.GET, entity, String.class).getBody();
    }

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
    HttpHeaders createHeadersWithJWT (String username, String password){
        return new HttpHeaders() {{
            //get the authorization bearer response
            setContentType(MediaType.APPLICATION_JSON);
            String token = getJWTToken(username, password);

            set( "Authorization", "Bearer "+ token);
        }};
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

    @PostMapping(value = "/follow")
    public ResponseEntity<String> callSecureService(@Valid @RequestBody SignInAndFollowRequest signInAndFollowRequest) {
        String url = remoteServerbaseURL +"/api/users/follow";
        String username = signInAndFollowRequest.getUsername();
        String password = signInAndFollowRequest.getPassword();

        FollowRequest followRequest = new FollowRequest(signInAndFollowRequest.getUsername_to_follow());

        ObjectMapper mapper = new ObjectMapper();

        String requestJson="";
        try{
            requestJson = mapper.writeValueAsString(followRequest);
        }catch(Exception e){
            logger.error(e.toString());
        }
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, createHeadersWithJWT(username,password));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }

}
