package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.controllers.IndexController;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ApplicationSecurityTestConfig.class)
@WebMvcTest(controllers = IndexController.class)
class IndexControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @WithMockUserRoleUser
    @Test
    public void indexPageUser() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
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