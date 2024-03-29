package com.bozo.issuetracker.controllers.mvc;

import com.bozo.issuetracker.controllers.ProjectController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.service.ProjectService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    ProjectService projectService;

    @Mock
    TeamService teamService;

    @InjectMocks
    ProjectController controller;

    MockMvc mockMvc;

    Project returnedProject;
    List<Project> returnedProjectList;

    @BeforeEach
    void setUp() {
        returnedProject = Project.builder().id(1L).assignedTeam(Team.builder().build()).build();
        returnedProjectList = List.of(returnedProject);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void allProjectList() throws Exception {
        when(projectService.findAll()).thenReturn(returnedProjectList);

        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.PROJECT_LIST.getPath()))
                .andExpect(model().attributeExists("projectList"));

        verifyNoMoreInteractions(projectService);
    }

    @Test
    void showProject() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(returnedProject);

        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/{projectId}", returnedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.PROJECT.getPath()))
                .andExpect(model().attributeExists("project"));

        verifyNoMoreInteractions(projectService);
    }

    @Test
    void addNewProject() throws Exception {
        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_PROJECT.getPath()))
                .andExpect(model().attributeExists("project"));
    }

    @Test
    void processAddingProject() throws Exception {
        Project project = Project.builder().id(2L).build();
        when(projectService.save(any())).thenReturn(project);

        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/project/" + project.getId()));

        verifyNoMoreInteractions(projectService);
    }

    @Test
    void editProject() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(returnedProject);

        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/{projectId}/edit", returnedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_PROJECT.getPath()))
                .andExpect(model().attribute("project", returnedProject));

        verifyNoMoreInteractions(projectService);
    }

    @Test
    void processEditingProject() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(returnedProject);
        when(projectService.save(any())).thenReturn(returnedProject);

        mockMvc.perform(post(Paths.PROJECT_PATH.getPath() + "/{projectId}/edit", returnedProject.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/project/" + returnedProject.getId()));

        verifyNoMoreInteractions(projectService);
    }

    @Test
    void deleteProject() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(returnedProject);

        mockMvc.perform(get(Paths.PROJECT_PATH.getPath() + "/{projectId}/delete", returnedProject.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/project/all"));

        verify(projectService).deleteById(anyLong());
    }
}