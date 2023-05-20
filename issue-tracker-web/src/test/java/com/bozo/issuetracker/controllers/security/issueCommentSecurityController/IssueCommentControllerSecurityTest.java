package com.bozo.issuetracker.controllers.security.issueCommentSecurityController;

import com.bozo.issuetracker.controllers.IssueCommentController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = IssueCommentController.class)
public class IssueCommentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueCommentSDJpaService commentService;

    @MockBean
    private IssueSDJpaService issueService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Test
    public void processAddingCommentUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.COMMENT_PATH.getPath() + "/new", 1))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(commentService);
    }

    @Test
    public void processEditingCommentUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.COMMENT_PATH.getPath() + "/1/edit", 1))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(commentService);
    }

    @Test
    public void deleteCommentUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.COMMENT_PATH.getPath() + "/1/delete", 1))
                .andExpect(status().is3xxRedirection());

        verify(commentService, times(0)).deleteById(anyLong());
    }
}
