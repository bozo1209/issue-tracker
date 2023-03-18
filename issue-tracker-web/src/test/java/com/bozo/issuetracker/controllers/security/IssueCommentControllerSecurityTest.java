package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.controllers.IssueCommentController;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.security.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.service.springdatajpa.IssueCommentSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.IssueSDJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = IssueCommentController.class)
public class IssueCommentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueCommentSDJpaService commentService;

    @MockBean
    private IssueSDJpaService issueService;

    private final String COMMENT_PATH = "/issue/1/comment";

    @WithMockUserRoleAdmin
    @Test
    public void processAddingCommentAdmin() throws Exception {
        Issue issue = Issue.builder().id(1L).build();
        IssueComment comment = IssueComment.builder().id(1L).build();
        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.save(any())).thenReturn(comment);
        mockMvc.perform(post(COMMENT_PATH + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processAddingCommentUser() throws Exception {
        Issue issue = Issue.builder().id(1L).build();
        IssueComment comment = IssueComment.builder().id(1L).build();
        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.save(any())).thenReturn(comment);
        mockMvc.perform(post(COMMENT_PATH + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processAddingCommentUnauthorized() throws Exception {
        mockMvc.perform(post(COMMENT_PATH + "/new"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(commentService);
    }

    @WithMockUserRoleAdmin
    @Test
    public void processEditingCommentAdmin() throws Exception {
        Issue issue = Issue.builder().id(1L).build();
        IssueComment comment = IssueComment.builder().id(1L).build();
        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.save(any())).thenReturn(comment);
        mockMvc.perform(post(COMMENT_PATH + "/{commentId}/edit", comment.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processEditingCommentUser() throws Exception {
        Issue issue = Issue.builder().id(1L).build();
        IssueComment comment = IssueComment.builder().id(1L).build();
        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.save(any())).thenReturn(comment);
        mockMvc.perform(post(COMMENT_PATH + "/{commentId}/edit", comment.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processEditingCommentUnauthorized() throws Exception {
        mockMvc.perform(post(COMMENT_PATH + "/1/edit"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(commentService);
    }

    @WithMockUserRoleAdmin
    @Test
    public void deleteCommentAdmin() throws Exception {
        IssueComment comment = IssueComment.builder().id(1L).build();
        when(issueService.findById(anyLong())).thenReturn(Issue.builder().build());
        when(commentService.save(any())).thenReturn(comment);
        mockMvc.perform(get(COMMENT_PATH + "/{commentId}/delete", comment.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void deleteCommentUser() throws Exception {
        IssueComment comment = IssueComment.builder().id(1L).build();
        when(issueService.findById(anyLong())).thenReturn(Issue.builder().build());
        when(commentService.save(any())).thenReturn(comment);
        mockMvc.perform(get(COMMENT_PATH + "/{commentId}/delete", comment.getId()))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void deleteCommentUnauthorized() throws Exception {
        mockMvc.perform(get(COMMENT_PATH + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(commentService, times(0)).deleteById(anyLong());
    }
}
