package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.controllers.pathsConfig.Paths;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}", returnedTeam.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.TEAM.getPath()))
                .andExpect(model().attributeExists("team"));

        verifyNoMoreInteractions(teamService);
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
    void deleteTeam() throws Exception {
        mockMvc.perform(get(Paths.TEAM_PATH.getPath() + "/{teamId}/delete", returnedTeam.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/team/all"));

        verify(teamService).deleteById(anyLong());
    }
}