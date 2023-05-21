package com.bozo.issuetracker.controllers.security.userSecurityController;

import com.bozo.issuetracker.controllers.UserController;
import com.bozo.issuetracker.controllers.config.ApplicationSecurityTestConfig;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.controllers.security.annotation.WithMockUserRoleAdmin;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.EncodePasswordForUser;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.springdatajpa.TeamSDJpaService;
import com.bozo.issuetracker.service.springdatajpa.UserSDJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ApplicationSecurityTestConfig.class})
@WebMvcTest(controllers = UserController.class)
public class UserControllerSecurityAdminTest {

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

    @WithMockUserRoleAdmin
    @Test
    public void allUserListAdmin() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/all"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
    @Test
    public void showUserByIdAdmin() throws Exception {
        when(userService.findById(anyLong())).thenReturn(User.builder().build());

        mockMvc.perform(get(Paths.USER_PATH.getPath() +"/1"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
    @Test
    public void addNewUserAdmin() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().isOk());
    }

    @WithMockUserRoleAdmin
    @Test
    public void processAddingUserAdmin() throws Exception {
        when(userService.save(any())).thenReturn(User.builder().id(1L).build());
        when(encodePasswordForUser.encodePasswordForUser(anyString())).thenReturn("pass");

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUserRoleAdmin
    @Test
    public void editUserAdmin() throws Exception {
        User user2 = User.builder().id(2L).build();

        when(userService.findById(anyLong())).thenReturn(user2);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/edit", user2.getId()))
                .andExpect(status().isOk());
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

    @WithMockUserRoleAdmin
    @Test
    public void deleteUserAmin() throws Exception {
        User user1 = User.builder().id(1L).build();

        when(userService.findById(anyLong())).thenReturn(user1);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/delete", user1.getId()))
                .andExpect(status().is3xxRedirection());
    }

}
