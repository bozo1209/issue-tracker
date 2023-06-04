package com.bozo.issuetracker.controllers.security.teamSecurityController;

import com.bozo.issuetracker.controllers.TeamController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.springdatajpa.TeamSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.UserSDJpaService;
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
@WebMvcTest(controllers = TeamController.class)
public class TeamControllerSecurityUserTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamSDJpaService teamService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @MockBean
    private UserSDJpaService userService;

    @WithMockUserRoleUser
    @Test
    public void allTeamListUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void showTeamUser() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(Team.builder().build());
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void addNewTeamUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleUser
    @Test
    public void processAddingTeamUser() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(teamService);
    }

    @WithMockUserRoleUser
    @Test
    public void editNewTeamUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleUser
    @Test
    public void processEditingTeamUser() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());

        verify(teamService, times(0)).save(any());
    }

    @WithMockUserRoleUser
    @Test
    public void addUserToTeamUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/user/{userId}", 1L,1L))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleUser
    @Test
    public void deleteTeamUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/delete"))
                .andExpect(status().isForbidden());

        verify(teamService, times(0)).deleteById(anyLong());
    }

}
