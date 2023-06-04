package com.bozo.issuetracker.controllers.security.teamSecurityController;

import com.bozo.issuetracker.controllers.TeamController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleTeamLeader;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.springdatajpa.TeamSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.UserSDJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
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
public class TeamControllerSecurityTeamLeaderTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamSDJpaService teamService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @MockBean
    private UserSDJpaService userService;

    @WithMockUserRoleTeamLeader
    @Test
    public void allTeamListTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void showTeamTeamLeader() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(Team.builder().build());
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void addNewTeamTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void processAddingTeamTeamLeader() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(teamService);
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void editNewTeamTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void processEditingTeamTeamLeader() throws Exception {
        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/1/edit"))
                .andExpect(status().isForbidden());

        verify(teamService, times(0)).save(any());
    }

    @WithUserDetails(value = "user-team_leader1", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void addUserToTeamTeamsLeader() throws Exception {
        Team team = Team.builder().id(1L).build();
        when(userService.findById(anyLong())).thenReturn(User.builder().build());
        when(teamService.findById(anyLong())).thenReturn(team);
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.TEAM_LEADER).build()));
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/user/{userId}", team.getId(),1L))
                .andExpect(status().is3xxRedirection());
    }

    @WithUserDetails(value = "user-team_leader2", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void addUserToTeamOtherLeader() throws Exception {
        Team team = Team.builder().id(1L).build();
        when(userService.findById(anyLong())).thenReturn(User.builder().build());
        when(teamService.findById(anyLong())).thenReturn(team);
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.TEAM_LEADER).build()));
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/user/{userId}", team.getId(),1L))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void deleteTeamTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/1/delete"))
                .andExpect(status().isForbidden());

        verify(teamService, times(0)).deleteById(anyLong());
    }

}
