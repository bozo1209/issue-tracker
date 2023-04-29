package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.controllers.ProjectController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.model.Team;
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
@WebMvcTest(controllers = ProjectController.class)
public class ProjectControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectSDJpaService projectService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @WithMockUserRoleAdmin
    @Test
    public void allProjectListAdmin() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void allProjectListUser() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void allProjectListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/all"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void showProjectAdmin() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().build());
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void showProjectUser() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().build());
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void showProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void addNewProjectAdmin() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void addNewProjectUser() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addNewProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processAddingProjectAdmin() throws Exception {
        Project project = Project.builder()
                                    .id(1L)
                                    .assignedTeam(Team.builder().build())
                                    .build();
        when(projectService.findById(anyLong())).thenReturn(project);
        when(projectService.save(any())).thenReturn(project);
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processAddingProjectUser() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(projectService);
    }

    @Test
    public void processAddingProjectUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(projectService);
    }

    @WithMockUserRoleAdmin
    @Test
    public void editNewProjectAdmin() throws Exception {
        Project project = Project.builder().id(1L).build();
        when(projectService.findById(anyLong())).thenReturn(project);
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/{projectId}/edit", project.getId()))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void editNewProjectUser() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void editNewProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processEditingProjectAdmin() throws Exception {
        Project project = Project.builder().id(1L).assignedTeam(Team.builder().build()).build();
        when(projectService.findById(anyLong())).thenReturn(project);
        when(projectService.save(any())).thenReturn(project);
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/{projectId}/edit", project.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processEditingProjectUser() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verify(projectService, times(0)).save(any());
    }

    @Test
    public void processEditingProjectUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verify(projectService, times(0)).save(any());
    }

    @WithMockUserRoleAdmin
    @Test
    public void deleteProjectAdmin() throws Exception {
        Project project = Project.builder()
                                    .id(1L)
                                    .assignedTeam(Team.builder().build())
                                    .build();
        when(projectService.findById(anyLong())).thenReturn(project);
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/{projectId}/delete", project.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void deleteProjectUser() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/delete"))
                .andExpect(status().isForbidden());

        verify(projectService, times(0)).deleteById(anyLong());
    }

    @Test
    public void deleteProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(projectService, times(0)).deleteById(anyLong());
    }

}
