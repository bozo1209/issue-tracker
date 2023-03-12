package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.config.ApplicationSecurityConfig;
import com.bozo.issuetracker.config.PasswordConfig;
import com.bozo.issuetracker.controllers.IndexController;
import com.bozo.issuetracker.controllers.security.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.repository.UserRepository;
import com.bozo.issuetracker.service.UserService;
import com.bozo.issuetracker.service.springdatajpa.UserSDJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;

//@Import({ApplicationSecurityConfig.class, PasswordConfig.class, ApplicationUserDetailsService.class, UserSDJpaService.class, UserRepository.class})
//@WebMvcTest(controllers = IndexController.class)
//@SpringBootTest
@Import(ApplicationSecurityTestConfig.class)
@WebMvcTest(controllers = IndexController.class)
class IndexControllerSecurityTest
//        extends BaseIT
{

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(roles = "USER")
    @Test
    public void indexPageUser() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void indexPageAdmin() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void indexPageUnauthorized() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

}