package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    String USER_PATH = "/user";

    @Mock
    UserService userService;

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

        mockMvc.perform(get(USER_PATH + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.USER_LIST.getPath()))
                .andExpect(model().attributeExists("userList"));

        verifyNoMoreInteractions(userService);
    }

    @Test
    void showUserById() throws Exception {
        when(userService.findById(anyLong())).thenReturn(returnedUser);

        mockMvc.perform(get(USER_PATH + "/{id}", returnedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.USER.getPath()))
                .andExpect(model().attributeExists("user"));

        verifyNoMoreInteractions(userService);
    }

    @Test
    void addNewUser() throws Exception {
        mockMvc.perform(get(USER_PATH + "/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(HTMLPaths.ADD_EDIT_USER.getPath()))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void processAddingUser() throws Exception {
        User user = User.builder().id(2L).build();
        when(userService.save(any())).thenReturn(user);

        mockMvc.perform(post(USER_PATH + "/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/" + user.getId()));

        verifyNoMoreInteractions(userService);
    }
}