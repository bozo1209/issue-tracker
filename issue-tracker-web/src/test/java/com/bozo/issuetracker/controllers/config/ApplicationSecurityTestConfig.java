package com.bozo.issuetracker.controllers.config;

import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.Team;
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
        Team team1 = Team.builder().id(1L).build();
        Team team2 = Team.builder().id(2L).build();
        User admin = User.builder()
                .id(1L)
                .userName("user-admin")
                .role(UserRoles.ADMIN)
                .password("{noop}user1")
                .build();
        User user1 = User.builder()
                .id(2L)
                .userName("user-user1")
                .role(UserRoles.USER)
                .password("{noop}user2")
                .memberOfTeam(team1)
                .build();
        User user2 = User.builder()
                .id(3L)
                .userName("user-user2")
                .role(UserRoles.USER)
                .password("{noop}user3")
                .build();
        User teamLeader1 = User.builder()
                .id(4L)
                .userName("user-team_leader1")
                .role(UserRoles.TEAM_LEADER)
                .password("{noop}user4")
                .memberOfTeam(team1)
                .leaderOfTeam(team1)
                .build();
        User teamLeader2 = User.builder()
                .id(5L)
                .userName("user-team_leader2")
                .role(UserRoles.TEAM_LEADER)
                .password("{noop}user4")
                .memberOfTeam(team2)
                .leaderOfTeam(team2)
                .build();
        team1.getMembers().add(user1);
        team1.getMembers().add(teamLeader1);
        return username -> Stream.of(
                    new ApplicationUser(admin),
                    new ApplicationUser(user1),
                    new ApplicationUser(user2),
                    new ApplicationUser(teamLeader1),
                    new ApplicationUser(teamLeader2))
                .filter(appUser ->
                        appUser
                                .getUser()
                                .getUserName()
                                .equalsIgnoreCase(username))
                .findFirst()
                .orElse(new ApplicationUser(User.builder().build()));
    }
}
