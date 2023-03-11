package com.bozo.issuetracker.config;

import com.bozo.issuetracker.enums.UserRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated());
        http.formLogin();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1 = User.builder()
                .username("user1")
                .password("{noop}user1")
                .roles(UserRoles.ADMIN.name())
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password("{noop}user2")
                .roles(UserRoles.USER.name())
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}