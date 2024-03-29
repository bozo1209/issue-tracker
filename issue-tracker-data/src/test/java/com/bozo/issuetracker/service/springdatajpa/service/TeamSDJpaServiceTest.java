package com.bozo.issuetracker.service.springdatajpa.service;

import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.repository.TeamRepository;
import com.bozo.issuetracker.service.springdatajpa.TeamSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamSDJpaServiceTest {

    public static final String TEAM_NAME = "team 1";
    public static final Long TEAM_ID = 1L;

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamSDJpaService service;

    Team returnedTeam;
    List<Team> returnedTeamList;

    @BeforeEach
    void setUp() {
        returnedTeam = Team.builder().id(TEAM_ID).teamName(TEAM_NAME).build();
        returnedTeamList = List.of(returnedTeam);
    }

    @Test
    void findAll() {
        when(teamRepository.findAll()).thenReturn(returnedTeamList);

        List<Team> teamList = service.findAll();

        assertNotNull(teamList);
        assertEquals(1, teamList.size());

        verify(teamRepository).findAll();
    }

    @Test
    void findById() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(returnedTeam));

        Team team = service.findById(TEAM_ID);

        assertNotNull(team);
        assertEquals(returnedTeam, team);
        assertEquals(returnedTeam.getId(), team.getId());

        verify(teamRepository).findById(anyLong());
    }

    @Test
    void findByIdNotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        Team team = service.findById(TEAM_ID);

        assertNull(team.getId());

        verify(teamRepository).findById(anyLong());
    }

    @Test
    void findByLeaderIdOrLeaderIsNullById(){
        when(teamRepository.findByLeaderIdOrLeaderIsNull(anyLong())).thenReturn(returnedTeamList);

        List<Team> teamList = service.findByLeaderIdOrLeaderIsNull(anyLong());

        assertNotNull(teamList);
        assertEquals(1, teamList.size());

        verify(teamRepository).findByLeaderIdOrLeaderIsNull(anyLong());
    }

    @Test
    void save() {
        String name = "team 2";
        Team teamToSave = Team.builder().id(2L).teamName(name).build();

        when(teamRepository.save(any())).thenReturn(teamToSave);

        Team savedTea = service.save(teamToSave);

        assertNotNull(savedTea);
        assertEquals(name, savedTea.getTeamName());

        verify(teamRepository).save(any());
    }

    @Test
    void delete() {
        service.delete(Team.builder().build());

        verify(teamRepository).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(TEAM_ID);

        verify(teamRepository).deleteById(anyLong());
    }
}