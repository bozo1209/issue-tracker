package com.bozo.issuetracker.controllers.security.issueSecurityController;

import com.bozo.issuetracker.controllers.IssueController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.springdatajpa.IssueSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.ProjectSDJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = IssueController.class)
public class IssueControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueSDJpaService issueService;

    @MockBean
    private ProjectSDJpaService projectService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Test
    public void allIssueListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/all"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void showIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void addNewIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processAddingIssueUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(issueService);
    }

    @Test
    public void editIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/1/edit", 1))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processEditingIssueUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/1/edit", 1))
                .andExpect(status().is3xxRedirection());

        verify(issueService, times(0)).save(any());
    }

    @Test
    public void deleteIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(issueService, times(0)).deleteById(anyLong());
    }

}
