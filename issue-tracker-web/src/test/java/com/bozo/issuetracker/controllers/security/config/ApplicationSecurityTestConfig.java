package com.bozo.issuetracker.controllers.security.config;

import com.bozo.issuetracker.enums.UserRoles;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableMethodSecurity
public class ApplicationSecurityTestConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1 = User.builder()
//                .username("user1")
//                .password("{noop}user1")
////                .password(passwordConfig.passwordEncoder().encode("user1"))
//                .roles(UserRoles.ADMIN.name())
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password("{noop}user1")
////                .password(passwordConfig.passwordEncoder().encode("user2"))
//                .roles(UserRoles.USER.name())
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

}
