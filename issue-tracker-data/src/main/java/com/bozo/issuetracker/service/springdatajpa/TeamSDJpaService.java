package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.repository.TeamRepository;
import com.bozo.issuetracker.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamSDJpaService implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public List<Team> findAll() {
        return null;
    }

    @Override
    public Team findById(Long aLong) {
        return null;
    }

    @Override
    public Team save(Team object) {
        return null;
    }

    @Override
    public void delete(Team object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
