package com.bozo.issuetracker.controllers.security.userSecurityController;

import com.bozo.issuetracker.controllers.UserController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleTeamLeader;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.details.user.EncodePasswordForUser;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.springdatajpa.TeamSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.UserSDJpaService;
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
@WebMvcTest(controllers = UserController.class)
public class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSDJpaService userService;

    @MockBean
    private TeamSDJpaService teamService;

    @MockBean
    private EncodePasswordForUser encodePasswordForUser;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Test
    public void allUserListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/all"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void showUserByIdUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void addNewUserUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processAddingUserUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(userService);
    }

    @Test
    public void editUserUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void processEditingUserUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(0)).save(any());
    }

    @Test
    public void deleteUserUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(0)).delete(any());
    }
}
