package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.controllers.UserController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleUser;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.details.user.EncodePasswordForUser;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.User;
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
    private EncodePasswordForUser encodePasswordForUser;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @WithMockUserRoleAdmin
    @Test
    public void allUserListAdmin() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void allUserListUser() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void allUserListUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/all"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void showUserByIdAdmin() throws Exception {
        when(userService.findById(anyLong())).thenReturn(User.builder().build());

        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void showUserByIdUser() throws Exception {
        when(userService.findById(anyLong())).thenReturn(User.builder().build());

        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void showUserByIdUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/1"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void addNewUserAdmin() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleUser
    @Test
    public void addNewUserUser() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addNewUserUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processAddingUserAdmin() throws Exception {
        when(userService.save(any())).thenReturn(User.builder().id(1L).build());
        when(encodePasswordForUser.encodePasswordForUser(anyString())).thenReturn("pass");

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void processAddingUserUser() throws Exception {
        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    public void processAddingUserUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(userService);
    }

    @WithMockUserRoleAdmin
    @Test
    public void editUserAdmin() throws Exception {
        User user2 = User.builder().id(2L).build();

        when(userService.findById(anyLong())).thenReturn(user2);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/edit", user2.getId()))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "user2", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void editUserUserWithSameId() throws Exception {
        User user2 = User.builder().id(2L).build();

        when(userService.findById(anyLong())).thenReturn(user2);
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.USER).build()));

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/edit", user2.getId()))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "user3", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void editUserUserWithDifferentId() throws Exception {
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.USER).build()));

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void editUserUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processEditingUserAdmin() throws Exception {
        User user2 = User.builder().id(2L).password("pass").build();

        when(userService.findById(anyLong())).thenReturn(user2);
        when(userService.save(any())).thenReturn(user2);
        when(encodePasswordForUser.encodePasswordForUser(anyString())).thenReturn("pass");

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/{userId}/edit", user2.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithUserDetails(value = "user2", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void processEditingUserUserWithSameId() throws Exception {
        User user2 = User.builder().id(2L).build();

        when(userService.findById(anyLong())).thenReturn(user2);
        when(userService.save(any())).thenReturn(user2);
        when(encodePasswordForUser.encodePasswordForUser(anyString())).thenReturn("pass");
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.USER).build()));

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/{userId}/edit", user2.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithUserDetails(value = "user3", userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void processEditingUserUserWithDifferentId() throws Exception {
        when(applicationUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new ApplicationUser(User.builder().role(UserRoles.USER).build()));

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).save(any());
    }

    @Test
    public void processEditingUserUnauthorized() throws Exception {
        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/2/edit"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(0)).save(any());
    }

    @WithMockUserRoleAdmin
    @Test
    public void deleteUserAmin() throws Exception {
        User user1 = User.builder().id(1L).build();

        when(userService.findById(anyLong())).thenReturn(user1);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/delete", user1.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleUser
    @Test
    public void deleteUserUser() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/1/delete"))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).delete(any());
    }

    @Test
    public void deleteUserUnauthorized() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/1/delete"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(0)).delete(any());
    }
}
