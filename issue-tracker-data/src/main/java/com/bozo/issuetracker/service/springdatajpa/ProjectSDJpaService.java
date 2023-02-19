package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.repository.ProjectRepository;
import com.bozo.issuetracker.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectSDJpaService implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseGet(Project::new);
    }

    @Override
    public Project save(Project object) {
        return projectRepository.save(object);
    }

    @Override
    public void delete(Project object) {
        projectRepository.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }
}
