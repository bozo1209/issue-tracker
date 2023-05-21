package com.bozo.issuetracker.controllers.security.userSecurityController;

import com.bozo.issuetracker.controllers.UserController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleTeamLeader;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = UserController.class)
public class UserControllerSecurityTeamLeaderTest {

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

    @WithMockUserRoleTeamLeader
    @Test
    public void allUserListTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void showUserByIdTeamLeader() throws Exception {
        when(userService.findById(anyLong())).thenReturn(User.builder().build());

        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void addNewUserTeamLeader() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());
    }

    @WithMockUserRoleTeamLeader
    @Test
    public void processAddingUserTeamLeader() throws Exception {
        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @WithUserDetails(value = "user-team_leader1", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void editUserUsersTeamLeader() throws Exception {
        User user2 = User.builder().id(2L).build();

        when(userService.findById(anyLong())).thenReturn(user2);
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.TEAM_LEADER).build()));

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/edit", user2.getId()))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "user-team_leader2", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void editUserDifferentTeamLeader() throws Exception {
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.TEAM_LEADER).build()));

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().isForbidden());
    }

    @WithUserDetails(value = "user-team_leader1", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void processEditingUserUsersTeamLeader() throws Exception {
        User user2 = User.builder().id(2L).build();

        when(userService.findById(anyLong())).thenReturn(user2);
        when(userService.save(any())).thenReturn(user2);
        when(encodePasswordForUser.encodePasswordForUser(anyString())).thenReturn("pass");
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.TEAM_LEADER).build()));

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/{userId}/edit", user2.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithUserDetails(value = "user-team_leader2", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void processEditingUserDifferentTeamLeader() throws Exception {
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.TEAM_LEADER).build()));

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).save(any());
    }

    @WithMockUserRoleUser
    @Test
    public void deleteUserUser() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/1/delete"))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).delete(any());
    }

}
