package com.bozo.issuetracker.config;

import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.enums.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurityConfig {

    private final PasswordConfig passwordConfig;
    private final ApplicationUserDetailsService applicationUserDetailsService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated());
        http.formLogin();
        http.authenticationProvider(authenticationProvider());
        http.csrf().ignoringRequestMatchers(PathRequest.toH2Console()).and().headers().frameOptions().sameOrigin(); // allow opening h2-console, fo dev purpose only
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(applicationUserDetailsService);
        provider.setPasswordEncoder(passwordConfig.passwordEncoder());
        return provider;
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1 = User.builder()
//                .username("user1")
//                .password(passwordConfig.passwordEncoder().encode("user1"))
//                .roles(UserRoles.ADMIN.name())
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password(passwordConfig.passwordEncoder().encode("user2"))
//                .roles(UserRoles.USER.name())
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
}
