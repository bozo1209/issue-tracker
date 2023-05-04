package com.bozo.issuetracker.controllers.mvc;

import com.bozo.issuetracker.controllers.UserController;
import com.bozo.issuetracker.controllers.config.Paths;
import com.bozo.issuetracker.details.user.EncodePasswordForUser;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.TeamService;
import com.bozo.issuetracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    TeamService teamService;

    @Mock
    EncodePasswordForUser encodePasswordForUser;

    @InjectMocks
    UserController controller;

    MockMvc mockMvc;

    User returnedUser;
    List<User> returnedUserList;

    @BeforeEach
    void setUp() {
        returnedUser = User.builder().id(1L).build();
        returnedUserList = List.of(returnedUser);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void allUserList() throws Exception {
        when(userService.findAll()).thenReturn(returnedUserList);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.USER_LIST.getPath()))
                .andExpect(model().attributeExists("userList"));

        verifyNoMoreInteractions(userService);
    }

    @Test
    void showUserById() throws Exception {
        when(userService.findById(anyLong())).thenReturn(returnedUser);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}", returnedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.USER.getPath()))
                .andExpect(model().attributeExists("user"));

        verifyNoMoreInteractions(userService);
    }

    @Test
    void addNewUser() throws Exception {
        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_USER.getPath()))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void processAddingUser() throws Exception {
        User user = User.builder().id(2L).build();
        when(userService.save(any())).thenReturn(user);

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/" + user.getId()));

        verifyNoMoreInteractions(userService);
    }

    @Test
    void editUser() throws Exception {
        when(userService.findById(anyLong())).thenReturn(returnedUser);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/edit", returnedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_USER.getPath()))
                .andExpect(model().attributeExists("user"));

        verifyNoMoreInteractions(userService);
    }

    @Test
    void processEditingUser() throws Exception {
        when(userService.findById(anyLong())).thenReturn(returnedUser);
        when(userService.save(any())).thenReturn(returnedUser);

        mockMvc.perform(post(Paths.USER_PATH.getPath() + "/{userId}/edit", returnedUser.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/" + returnedUser.getId()));

        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUser() throws Exception {
        when(userService.findById(anyLong())).thenReturn(returnedUser);

        mockMvc.perform(get(Paths.USER_PATH.getPath() + "/{userId}/delete", returnedUser.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/all"));

        verify(userService).delete(any());
    }
}