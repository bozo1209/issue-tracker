package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.controllers.IssueController;
import com.bozo.issuetracker.controllers.pathsConfig.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.security.config.ApplicationSecurityTestConfig;
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

    @WithMockUserRoleAdmin
    @Test
    public void allIssueListAdmin() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void allIssueListUser() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void allIssueListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/all"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void showIssueAdmin() throws Exception {
        Issue issue = Issue.builder().id(1L).build();

        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void showIssueUser() throws Exception {
        Issue issue = Issue.builder().id(1L).build();

        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void showIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/1"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void addNewIssueAdmin() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());

        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void addNewIssueUser() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());

        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void addNewIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
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

        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processAddingIssueUser() throws Exception {
        Project project = Project.builder().id(1L).build();
        Issue issue = Issue.builder()
                                .id(1L)
                                .issueCreator(User.builder().build())
                                .build();

        when(issueService.findById(anyLong())).thenReturn(issue);
        when(issueService.save(any())).thenReturn(issue);
        when(projectService.findById(anyLong())).thenReturn(project);

        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processAddingIssueUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(issueService);
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

    @WithMockUserRoleUser
    @Test
    public void editIssueUser() throws Exception {
        Issue issue = Issue.builder().id(1L).build();

        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());
        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/{issueId}/edit", 1, issue.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void editIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/1/edit", 1))
                .andExpect(status().is3xxRedirection());
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

    @WithMockUserRoleUser
    @Test
    public void processEditingIssueUser() throws Exception {
        Issue issue = Issue.builder().id(1L).build();

        when(issueService.findById(anyLong())).thenReturn(issue);
        when(issueService.save(any())).thenReturn(issue);

        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/{issueId}/edit", 1, issue.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processEditingIssueUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/1/edit", 1))
                .andExpect(status().is3xxRedirection());

        verify(issueService, times(0)).save(any());
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

    @WithMockUserRoleUser
    @Test
    public void deleteIssueUser() throws Exception {
        Issue issue = Issue.builder()
                                .id(1L)
                                .issueCreator(User.builder().build())
                                .project(Project.builder().id(1L).build())
                                .build();

        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/{issueId}/delete", issue.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void deleteIssueUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(issueService, times(0)).deleteById(anyLong());
    }

}
