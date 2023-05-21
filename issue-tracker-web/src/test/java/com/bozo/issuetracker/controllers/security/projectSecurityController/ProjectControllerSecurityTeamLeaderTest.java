package com.bozo.issuetracker.controllers.security.projectSecurityController;

import com.bozo.issuetracker.controllers.ProjectController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleTeamLeader;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.service.springdatajpa.ProjectSDJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = ProjectController.class)
public class ProjectControllerSecurityTeamLeaderTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectSDJpaService projectService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @WithMockUserRoleTeamLeader
    @Test
    public void allProjectListTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void showProjectTeamLeader() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().build());
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void addNewProjectTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void processAddingProjectTeamLeader() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(projectService);
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void editNewProjectTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void processEditingProjectTeamLeader() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verify(projectService, times(0)).save(any());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void deleteProjectTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/delete"))
                .andExpect(status().isForbidden());

        verify(projectService, times(0)).deleteById(anyLong());
    }

}
