package com.spring.twitterconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;

/**
 * Created by ritakuo on 6/22/19.
 */
@RestController
public class ConsumeWebService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${remoteServerUrl}")
    private String remoteServerURL;

    @Value("${remoteServerPort}")
    private String remoteServerPort;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    @RequestMapping(value = "/users/{username}/tweet")
    public String getUserTweetList(@PathVariable("username") String username) {
        System.out.println("line 35 ");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        return restTemplate.exchange(
                remoteServerURL +":"+ remoteServerPort +"/api/users/"+ username+"/tweets", HttpMethod.GET, entity, String.class).getBody();
    }

    @RequestMapping(value = "/users/getAccessToken")
    public ResponseEntity<String> callSecureService() {
        String url = remoteServerURL +":"+ remoteServerPort +"/api/auth/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"username\":\"rita\", \"password\":\"secret\"}";

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }




}
