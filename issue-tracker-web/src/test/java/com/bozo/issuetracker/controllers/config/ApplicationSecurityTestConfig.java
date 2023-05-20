package com.bozo.issuetracker.controllers.config;

import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import java.util.stream.Stream;

@TestConfiguration
@EnableMethodSecurity
public class ApplicationSecurityTestConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated());
        http.formLogin();
        http.csrf().disable();
        return http.build();
    }

    @Bean(name = "testUserDetailsService")
    public UserDetailsService testUserDetailsService() {
        return username -> Stream.of(
                    new ApplicationUser(User.builder()
                            .id(1L)
                            .userName("user-admin")
                            .role(UserRoles.ADMIN)
                            .password("{noop}user1")
                            .build()),
                    new ApplicationUser(User.builder()
                            .id(2L)
                            .userName("user-user1")
                            .role(UserRoles.USER)
                            .password("{noop}user2")
                            .build()),
                    new ApplicationUser(User.builder()
                            .id(3L)
                            .userName("user-user2")
                            .role(UserRoles.USER)
                            .password("{noop}user3")
                            .build()),
                    new ApplicationUser(User.builder()
                            .id(4L)
                            .userName("user-team_leader")
                            .role(UserRoles.TEAM_LEADER)
                            .password("{noop}user4")
                            .build()))
                .filter(appUser ->
                        appUser
                                .getUser()
                                .getUserName()
                                .equalsIgnoreCase(username))
                .findFirst()
                .orElse(new ApplicationUser(User.builder().build()));
    }
}
