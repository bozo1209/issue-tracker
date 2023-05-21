package com.bozo.issuetracker.controllers.security.issueCommentSecurityController;

import com.bozo.issuetracker.controllers.IssueCommentController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleTeamLeader;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.springdatajpa.IssueCommentSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.IssueSDJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = IssueCommentController.class)
public class IssueCommentControllerSecurityTeamLeaderTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueCommentSDJpaService commentService;

    @MockBean
    private IssueSDJpaService issueService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @WithUserDetails(value = "user-team_leader1", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void processAddingCommentTeamLeader() throws Exception {
        Issue issue = Issue.builder().id(1L).build();
        IssueComment comment = IssueComment.builder().id(1L).build();

        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.save(any())).thenReturn(comment);
        when(applicationUserDetailsService.loadUserByUsername(anyString())).thenReturn(new ApplicationUser(User.builder().role(UserRoles.ADMIN).build()));

        mockMvc.perform(post(Paths.COMMENT_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void processEditingCommentTeamLeader() throws Exception {
        Issue issue = Issue.builder().id(1L).build();
        IssueComment comment = IssueComment.builder().id(1L).commentCreator(User.builder().build()).build();

        when(issueService.findById(anyLong())).thenReturn(issue);
        when(commentService.findById(anyLong())).thenReturn(comment);
        when(commentService.save(any())).thenReturn(comment);

        mockMvc.perform(post(Paths.COMMENT_PATH.getPath() + "/{commentId}/edit", 1, comment.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void deleteCommentTeamLeader() throws Exception {
        IssueComment comment = IssueComment.builder().id(1L).build();

        when(issueService.findById(anyLong())).thenReturn(Issue.builder().build());
        when(commentService.save(any())).thenReturn(comment);

        mockMvc.perform(get(Paths.COMMENT_PATH.getPath() + "/{commentId}/delete", 1, comment.getId()))
                .andExpect(status().is3xxRedirection());
    }

}
