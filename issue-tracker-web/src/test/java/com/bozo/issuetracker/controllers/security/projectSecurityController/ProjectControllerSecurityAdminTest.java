package com.bozo.issuetracker.controllers.security.projectSecurityController;

import com.bozo.issuetracker.controllers.ProjectController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = ProjectController.class)
public class ProjectControllerSecurityAdminTest {

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

    @WithMockUserRoleAdmin
    @Test
    public void showProjectAdmin() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().build());
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
    @Test
    public void addNewProjectAdmin() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isOk());
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

    @WithMockUserRoleAdmin
    @Test
    public void editNewProjectAdmin() throws Exception {
        Project project = Project.builder().id(1L).build();
        when(projectService.findById(anyLong())).thenReturn(project);
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/{projectId}/edit", project.getId()))
                .andExpect(status().isOk());
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

}
