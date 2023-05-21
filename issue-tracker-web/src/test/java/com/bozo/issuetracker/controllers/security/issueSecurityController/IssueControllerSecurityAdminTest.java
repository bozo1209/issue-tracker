package com.bozo.issuetracker.controllers.security.issueSecurityController;

import com.bozo.issuetracker.controllers.IssueController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = IssueController.class)
public class IssueControllerSecurityAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueSDJpaService issueService;

    @MockBean
    private ProjectSDJpaService projectService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @WithMockUserRoleAdmin
    @Test
    public void allIssueListAdmin() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
    @Test
    public void showIssueAdmin() throws Exception {
        Issue issue = Issue.builder().id(1L).project(Project.builder().id(1L).build()).build();

        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
    @Test
    public void addNewIssueAdmin() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());

        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "user-admin", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void processAddingIssueAdmin() throws Exception {
        Project project = Project.builder().id(1L).build();
        Issue issue = Issue.builder()
                .id(1L)
                .issueCreator(User.builder().build())
                .build();

        when(issueService.findById(anyLong())).thenReturn(issue);
        when(issueService.save(any())).thenReturn(issue);
        when(projectService.findById(anyLong())).thenReturn(project);
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.ADMIN).build()));

        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void editIssueAdmin() throws Exception {
        Issue issue = Issue.builder().id(1L).build();

        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());
        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/{issueId}/edit", 1, issue.getId()))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processEditingIssueAdmin() throws Exception {
        Issue issue = Issue.builder().id(1L).build();

        when(issueService.findById(anyLong())).thenReturn(issue);
        when(issueService.save(any())).thenReturn(issue);

        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/{issueId}/edit", 1, issue.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void deleteIssueAdmin() throws Exception {
        Issue issue = Issue.builder()
                .id(1L)
                .issueCreator(User.builder().build())
                .project(Project.builder().id(1L).build())
                .build();

        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/{issueId}/delete", issue.getId()))
                .andExpect(status().is3xxRedirection());
    }

}
