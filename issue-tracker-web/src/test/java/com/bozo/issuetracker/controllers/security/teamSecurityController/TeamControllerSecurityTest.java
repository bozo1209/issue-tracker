package com.bozo.issuetracker.controllers.security.teamSecurityController;

import com.bozo.issuetracker.controllers.TeamController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.service.springdatajpa.TeamSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.UserSDJpaService;
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
@WebMvcTest(controllers = TeamController.class)
public class TeamControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamSDJpaService teamService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @MockBean
    private UserSDJpaService userService;

    @Test
    public void allTeamListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/all"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void showTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void addNewTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processAddingTeamUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(teamService);
    }

    @Test
    public void editNewTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processEditingTeamUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().is3xxRedirection());

        verify(teamService, times(0)).save(any());
    }

    @Test
    public void addUserToTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/user/{userId}", 1L,1L))
                .andExpect(status().is3xxRedirection());
    }

    @Test void setNewTeamLeaderUnauthorized() throws Exception{
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/setleader/{userId}", 1L, 1L))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void deleteTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(teamService, times(0)).deleteById(anyLong());
    }
}
