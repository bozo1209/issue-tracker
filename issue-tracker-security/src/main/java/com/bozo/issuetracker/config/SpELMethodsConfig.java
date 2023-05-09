package com.bozo.issuetracker.config;

import com.bozo.issuetracker.model.User;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.function.Function;

@Configuration
public class SpELMethodsConfig {

    @Bean
    public StandardEvaluationContext standardEvaluationContext(){
        return new StandardEvaluationContext();
    }

    @SneakyThrows
    @Bean
    public boolean mySpELConfig(StandardEvaluationContext context){
        context.registerFunction("userToUserId", SpELMethodsConfig.class.getMethod("userToUserId"));
        return true;
    }

    public static Function<User, Long> userToUserId(){
        return User::getId;
    }

}
