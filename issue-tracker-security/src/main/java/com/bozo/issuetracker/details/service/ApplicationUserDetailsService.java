package com.bozo.issuetracker.details.service;

import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new ApplicationUser(
                Optional
                        .ofNullable(userService.findByUserName(username))
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        String.format("user: %s not exist", username)
                                )
                        )
        );
    }
}
