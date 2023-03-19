package com.bozo.issuetracker.details.user;

import com.bozo.issuetracker.config.PasswordConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EncodePasswordForUser {

    private PasswordConfig passwordConfig;

    public String encodePasswordForUser(String password){
        return passwordConfig.passwordEncoder().encode(password);
    }
}
