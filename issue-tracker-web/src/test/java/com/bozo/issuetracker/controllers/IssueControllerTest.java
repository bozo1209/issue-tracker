package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.service.IssueService;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IssueControllerTest {

    @Mock
    IssueService issueService;

    @InjectMocks
    IssueController controller;

    MockMvc mockMvc;

    Issue returnedIssue;
    List<Issue> returnedIssueList;

    @BeforeEach
    void setUp() {
        returnedIssue = Issue.builder().id(1L).build();
        returnedIssueList = List.of(returnedIssue);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void allIssueList() throws Exception {
        when(issueService.findAll()).thenReturn(returnedIssueList);

        mockMvc.perform(get("/issue/all"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ISSUE_LIST.getPath()))
                .andExpect(model().attributeExists("issueList"));

        verifyNoMoreInteractions(issueService);
    }
}