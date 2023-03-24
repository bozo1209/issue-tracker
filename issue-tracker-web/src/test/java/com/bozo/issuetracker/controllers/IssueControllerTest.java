package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.controllers.pathsConfig.Paths;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.IssueService;
import com.bozo.issuetracker.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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
class IssueControllerTest {

    @Mock
    IssueService issueService;

    @Mock
    ProjectService projectService;

    @InjectMocks
    IssueController controller;

    MockMvc mockMvc;

    Issue returnedIssue;
    List<Issue> returnedIssueList;

    @BeforeEach
    void setUp() {
        returnedIssue = Issue.builder().id(1L).build();
        returnedIssue.setIssueCreator(User.builder().id(1L).build());
        returnedIssueList = List.of(returnedIssue);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void allIssueList() throws Exception {
        when(issueService.findAll()).thenReturn(returnedIssueList);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() +"/all"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ISSUE_LIST.getPath()))
                .andExpect(model().attributeExists("issueList"));

        verifyNoMoreInteractions(issueService);
    }

    @Test
    void showIssue() throws Exception {
        when(issueService.findById(anyLong())).thenReturn(returnedIssue);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() +"/{issueId}", returnedIssue.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ISSUE.getPath()))
                .andExpect(model().attributeExists("issue"));

        verifyNoMoreInteractions(issueService);
    }

    @Test
    void addNewIssue() throws Exception {
        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());

        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_ISSUE.getPath()))
                .andExpect(model().attributeExists("issue"))
                .andExpect(model().attributeExists("project"));
    }

    @Test
    void processAddingIssue() throws Exception {
        Issue issue = Issue.builder().id(2L).build();

        when(issueService.save(any())).thenReturn(issue);
        when(issueService.findById(anyLong())).thenReturn(returnedIssue);
        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());

        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/issue/" + issue.getId()));

        verifyNoMoreInteractions(issueService);
    }

    @Test
    void editIssue() throws Exception {
        when(issueService.findById(anyLong())).thenReturn(returnedIssue);
        when(projectService.findById(anyLong())).thenReturn(Project.builder().id(1L).build());

        mockMvc.perform(get(Paths.PROJECT_ISSUE_PATH.getPath() +"/{issueId}/edit", 1, returnedIssue.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_ISSUE.getPath()))
                .andExpect(model().attribute("issue", returnedIssue))
                .andExpect(model().attributeExists("project"));

        verifyNoMoreInteractions(issueService);
    }

    @Test
    void processEditingIssue() throws Exception {
        when(issueService.findById(anyLong())).thenReturn(returnedIssue);
        when(issueService.save(any())).thenReturn(returnedIssue);

        mockMvc.perform(post(Paths.PROJECT_ISSUE_PATH.getPath() +"/{issueId}/edit", 1, returnedIssue.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/issue/" + returnedIssue.getId()));

        verifyNoMoreInteractions(issueService);
    }

    @Test
    void deleteIssue() throws Exception {
        returnedIssue.setProject(Project.builder().id(1L).build());

        when(issueService.findById(anyLong())).thenReturn(returnedIssue);

        mockMvc.perform(get(Paths.ISSUE_PATH.getPath() +"/{issueId}/delete", returnedIssue.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/project/" + returnedIssue.getProject().getId()));

        verify(issueService).deleteById(anyLong());
    }
}