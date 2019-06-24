package com.spring.twitterapp;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ritakuo on 6/21/19.
 */
//configuration- enable swagger
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //docket, swagger2, all the paths, all the apis
    //http://localhost:8080/v2/api-docs
    //http://localhost:8080/swagger-ui.html

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "api documentation",
            "api documentation",
            "1.0",
            "urn:tos",
            "rita",
            "apache 2.0",
            "http://www.apache.org/license/license-2.0");

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT_API_INFO).select()
//                .apis(RequestHandlerSelectors.basePackage("com.spring.twitterapp.controller"))
//                .paths(PathSelectors.regex("/.*")).build();
//    }


    @Bean
    public Docket apiDocket() {

        Docket docket =  new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT_API_INFO)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spring.twitterapp.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Lists.newArrayList(apiKey()));

        return docket;

    }
    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }



}
