package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.config.ApplicationSecurityConfig;
import com.bozo.issuetracker.config.PasswordConfig;
import com.bozo.issuetracker.controllers.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityConfig.class, PasswordConfig.class})
@WebMvcTest(controllers = IndexController.class)
class IndexControllerSecurityTest {

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