package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.repository.ProjectRepository;
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
class ProjectSDJpaServiceTest {

    public static final String PROJECT_NAME = "project 1";
    public static final Long PROJECT_ID = 1L;

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectSDJpaService service;

    Project returnedProject;

    @BeforeEach
    void setUp() {
        returnedProject = Project.builder().id(PROJECT_ID).projectName(PROJECT_NAME).build();
    }

    @Test
    void findAll() {
        List<Project> returnedProjectList = List.of(returnedProject);

        when(projectRepository.findAll()).thenReturn(returnedProjectList);

        List<Project> projectList = service.findAll();

        assertNotNull(projectList);
        assertEquals(1, returnedProjectList.size());

        verify(projectRepository).findAll();
    }

    @Test
    void findById() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(returnedProject));

        Project project = service.findById(PROJECT_ID);

        assertNotNull(project);
        assertEquals(returnedProject, project);
        assertEquals(returnedProject.getId(), project.getId());

        verify(projectRepository).findById(anyLong());
    }

    @Test
    void findByIdNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        Project project = service.findById(PROJECT_ID);

        assertNull(project.getId());

        verify(projectRepository).findById(anyLong());
    }

    @Test
    void save() {
        String name = "project 2";
        Project projectToSave = Project.builder().id(2L).projectName(name).build();

        when(projectRepository.save(any())).thenReturn(projectToSave);

        Project savedProject = service.save(projectToSave);

        assertNotNull(savedProject);
        assertEquals(name, savedProject.getProjectName());

        verify(projectRepository).save(any());
    }

    @Test
    void delete() {
        service.delete(Project.builder().build());

        verify(projectRepository).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(PROJECT_ID);

        verify(projectRepository).deleteById(anyLong());
    }
}