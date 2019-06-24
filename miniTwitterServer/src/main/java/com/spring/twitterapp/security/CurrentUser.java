package com.spring.twitterapp.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * creates the @currentUser annotation based on the @AuthenticationPrincipal annotion from spring
 */

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal

public @interface CurrentUser {
}
