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
        return teamRepository.findAll();
    }

    @Override
    public Team findById(Long id) {
        return teamRepository.findById(id).orElseGet(Team::new);
    }

    @Override
    public Team save(Team object) {
        return teamRepository.save(object);
    }

    @Override
    public void delete(Team object) {
        teamRepository.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
}
