package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.service.IssueCommentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IssueCommentControllerTest {

    @Mock
    IssueCommentService commentService;

    @Mock
    IssueService issueService;

    @InjectMocks
    IssueCommentController controller;

    MockMvc mockMvc;

    Issue issue;
    IssueComment returnedComment;
    List<IssueComment> returnedIssueCommentList;

    @BeforeEach
    void setUp() {
        issue = Issue.builder().id(1L).build();
        returnedComment = IssueComment.builder().id(1L).issue(issue).build();

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void processAddingComment() throws Exception{
        IssueComment comment = IssueComment.builder().id(2L).issue(issue).build();
        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.save(any())).thenReturn(comment);

        mockMvc.perform(post("/issue/{issueId}/comment/new", issue.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/issue/" + issue.getId()));

        verifyNoMoreInteractions(commentService);
    }

    @Test
    void processEditingComment() throws Exception{
        returnedComment.setComment("edit");
        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.save(any())).thenReturn(returnedComment);

        mockMvc.perform(post("/issue/{issueId}/comment/{commentId}/edit", issue.getId(), returnedComment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/issue/" + issue.getId()));

        verifyNoMoreInteractions(commentService);
    }

    @Test
    void deleteComment() throws Exception{
        when(commentService.findById(anyLong())).thenReturn(returnedComment);
        when(issueService.findById(anyLong())).thenReturn(issue);

        mockMvc.perform(get("/issue/{issueId}/comment/{commentId}/delete", issue.getId(), returnedComment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/issue/" + issue.getId()));

        verify(commentService).findById(anyLong());
    }
}