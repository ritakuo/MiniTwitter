package com.spring.twitterapp.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/***
 * Spring security provides an annotation called @AuthenticationPrincipal to access the currently authenticated user in the controllers.
 *
 * The following CurrentUser annotation is a wrapper around @AuthenticationPrincipal annotation.
 * We’ve created a meta-annotation so that we don’t get too much tied up of with Spring Security related annotations everywhere in our project.
 * This reduces the dependency on Spring Security
 */

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal

public @interface CurrentUser {
}
