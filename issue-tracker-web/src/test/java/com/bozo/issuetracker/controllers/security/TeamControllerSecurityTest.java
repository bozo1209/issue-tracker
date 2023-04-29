package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.controllers.TeamController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.service.springdatajpa.TeamSDJpaService;
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

    @WithMockUserRoleAdmin
    @Test
    public void allTeamListAdmin() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void allTeamListUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void allTeamListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/all"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void showTeamAdmin() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(Team.builder().build());
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void showTeamUser() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(Team.builder().build());
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void showTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void addNewTeamAdmin() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void addNewTeamUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addNewTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processAddingTeamAdmin() throws Exception {
        when(teamService.save(any())).thenReturn(Team.builder().id(1L).build());
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processAddingTeamUser() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(teamService);
    }

    @Test
    public void processAddingTeamUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(teamService);
    }

    @WithMockUserRoleAdmin
    @Test
    public void editNewTeamAdmin() throws Exception {
        Team team = Team.builder().id(1L).build();
        when(teamService.findById(anyLong())).thenReturn(team);
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/edit", team.getId()))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void editNewTeamUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void editNewTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processEditingTeamAdmin() throws Exception {
        Team team = Team.builder().id(1L).build();
        when(teamService.findById(anyLong())).thenReturn(team);
        when(teamService.save(any())).thenReturn(team);
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/{teamId}/edit", team.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processEditingTeamUser() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());

        verify(teamService, times(0)).save(any());
    }

    @Test
    public void processEditingTeamUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().is3xxRedirection());

        verify(teamService, times(0)).save(any());
    }

    @WithMockUserRoleAdmin
    @Test
    public void deleteTeamAdmin() throws Exception {
        Team team = Team.builder().id(1L).build();
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/delete", team.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void deleteTeamUser() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/delete"))
                .andExpect(status().isForbidden());

        verify(teamService, times(0)).deleteById(anyLong());
    }

    @Test
    public void deleteTeamUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(teamService, times(0)).deleteById(anyLong());
    }
}
