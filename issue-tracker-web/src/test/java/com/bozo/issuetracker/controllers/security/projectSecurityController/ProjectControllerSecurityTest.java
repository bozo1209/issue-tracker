package com.bozo.issuetracker.controllers.security.projectSecurityController;

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

    @Test
    public void allProjectListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/all"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void showProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void addNewProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processAddingProjectUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(projectService);
    }

    @Test
    public void editNewProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processEditingProjectUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verify(projectService, times(0)).save(any());
    }

    @Test
    public void deleteProjectUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(projectService, times(0)).deleteById(anyLong());
    }

}
