package com.bozo.issuetracker.controllers.mvc;

import com.bozo.issuetracker.controllers.TeamController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.TeamService;
import com.bozo.issuetracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Mock
    TeamService teamService;

    @Mock
    UserService userService;

    @InjectMocks
    TeamController controller;

    MockMvc mockMvc;

    Team returnedTeam;
    List<Team> returnedTeamList;

    @BeforeEach
    void setUp() {
        returnedTeam = Team.builder().id(1L).build();
        returnedTeamList = List.of(returnedTeam);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void allTeamList() throws Exception {
        when(teamService.findAll()).thenReturn(returnedTeamList);

        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.TEAM_LIST.getPath()))
                .andExpect(model().attributeExists("teamList"));

        verifyNoMoreInteractions(teamService);
    }

    @Test
    void showTeam() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(returnedTeam);
        when(userService.findByMemberOfTeamIsNull()).thenReturn(List.of());

        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}", returnedTeam.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.TEAM.getPath()))
                .andExpect(model().attributeExists("team"))
                .andExpect(model().attributeExists("usersWithoutTeam"));

        verifyNoMoreInteractions(teamService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void addNewTeam() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_TEAM.getPath()))
                .andExpect(model().attributeExists("team"));
    }

    @Test
    void processAddingTeam() throws Exception {
        Team team = Team.builder().id(2L).build();
        when(teamService.save(any())).thenReturn(team);

        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/team/" + team.getId()));

        verifyNoMoreInteractions(teamService);
    }

    @Test
    void editTeam() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(returnedTeam);

        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/edit", returnedTeam.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_TEAM.getPath()))
                .andExpect(model().attribute("team", returnedTeam));

        verifyNoMoreInteractions(teamService);
    }

    @Test
    void processEditingTeam() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(returnedTeam);
        when(teamService.save(any())).thenReturn(returnedTeam);

        mockMvc.perform(post(Paths.TEAM_PATH.getPath() + "/{teamId}/edit", returnedTeam.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/team/" + returnedTeam.getId()));

        verifyNoMoreInteractions(teamService);
    }

    @Test
    void addUserToTeam() throws Exception {
//        User leader = User.builder().id(1L).memberOfTeam(returnedTeam).leaderOfTeam(returnedTeam).build();
//        User member = User.builder().id(2L).build();
//        returnedTeam.getMembers().add(leader);
//        returnedTeam.setLeader(leader);
//        ApplicationUser applicationUser = new ApplicationUser(leader);
        when(userService.findById(anyLong())).thenReturn(User.builder().build());
        when(teamService.findById(anyLong())).thenReturn(returnedTeam);
        when(teamService.save(any())).thenReturn(returnedTeam);
//        when(userService);
//        doNothing().when(userService).updateUserInCache(User.builder().userName("name").build());
//        doAnswer(i -> {return null}).when(userService.updateUserInCache(User.builder().build()));
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(applicationUser, applicationUser.getPassword(), applicationUser.getAuthorities()));

        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/user/{userId}", returnedTeam.getId(), 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/team/" + returnedTeam.getId()));

        verifyNoMoreInteractions(teamService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteTeam() throws Exception {
        when(teamService.findById(anyLong())).thenReturn(returnedTeam);

        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/delete", returnedTeam.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/team/all"));

        verify(teamService).deleteById(anyLong());
    }
}