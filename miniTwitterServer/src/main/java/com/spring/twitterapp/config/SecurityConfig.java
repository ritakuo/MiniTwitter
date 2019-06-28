package com.spring.twitterapp.config;


import com.spring.twitterapp.security.CustomUserDetailsService;
import com.spring.twitterapp.security.JwtAuthenticationEntryPoint;
import com.spring.twitterapp.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//enable method level security based on annotations.
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        //enables the @Secured annotation using which you can protect your controller/service
        securedEnabled = true,
        //enables the @RolesAllowed
        jsr250Enabled = true,
        //enables @PreAuthorize and @PostAuthorize annotations
        prePostEnabled = true
)

/*
WebSecurityConfigurerAdapter implements Spring Security’s WebSecurityConfigurer interface.
It provides default security configurations.
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //To authenticate a User or perform various role-based checks
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    //This class return a 401 unauthorized error to clients that try to access a protected resource
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    /*
        We’ll use JWTAuthenticationFilter to implement a filter that -
        reads JWT authentication token from the Authorization header of all the requests
        validates the token
        loads the user details associated with that token.
        Sets the user details in Spring Security’s SecurityContext. Spring Security uses the user details to perform authorization checks.
        We can also access the user details stored in the SecurityContext in our controllers to perform our business logic.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    /*
        AuthenticationManagerBuilder is used to create an AuthenticationManager instance which is the main Spring Security interface for authenticating a user.
        You can use AuthenticationManagerBuilder to build in-memory authentication, LDAP authentication, JDBC authentication, or add your custom authentication provider.
        In our example, we’ve provided our customUserDetailsService and a passwordEncoder to build the AuthenticationManager.
        We’ll use the configured AuthenticationManager to authenticate a user in the login API.
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
        The HttpSecurity configurations are used to configure security functionalities like csrf, sessionManagement, and add rules to protect resources based on various conditions.
        In our example, we’re permitting access to static resources and few other public APIs to everyone and restricting access to other APIs to authenticated users only.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/v2/api-docs")
                .permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
                .permitAll()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/tweets/**", "/api/users/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


    }
}
