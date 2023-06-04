package com.bozo.issuetracker.service.springdatajpa.service;

import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.repository.UserRepository;
import com.bozo.issuetracker.service.springdatajpa.UserSDJpaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSDJpaServiceTest {

    private static final String USER_NAME = "smith";
    private static final Long USER_ID = 1L;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserSDJpaService service;

    User returnedUser;

    @BeforeEach
    void setUp() {
        returnedUser = User.builder().id(USER_ID).userName(USER_NAME).build();
    }

    @Test
    void findAll() {
        List<User> returnedUserList = List.of(returnedUser);

        when(userRepository.findAll()).thenReturn(returnedUserList);

        List<User> userList = service.findAll();

        assertNotNull(userList);
        assertEquals(1, userList.size());

        verify(userRepository).findAll();
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(returnedUser));

        User smith = service.findById(USER_ID);

        assertNotNull(smith);
        assertEquals(USER_NAME, smith.getUserName());

        verify(userRepository).findById(anyLong());
    }

    @Test
    void findByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        User smith = service.findById(USER_ID);

        assertNull(smith.getId());

        verify(userRepository).findById(anyLong());
    }

    @Test
    void findByName() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(returnedUser));

        User smith = service.findByUserName(USER_NAME);

        assertNotNull(smith);
        assertEquals(USER_NAME, smith.getUserName());

        verify(userRepository).findByUserName(anyString());
    }

    @Test
    void findByNameNotFound() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        User smith = service.findByUserName(USER_NAME);

        assertNull(smith.getId());

        verify(userRepository).findByUserName(anyString());
    }

    @Test
    void findByMemberOfTeamIsNull(){
        List<User> returnedUserList = List.of(returnedUser);

        when(userRepository.findByMemberOfTeamIsNull()).thenReturn(returnedUserList);

        List<User> userList = service.findByMemberOfTeamIsNull();

        assertNotNull(userList);
        assertEquals(1, userList.size());

        verify(userRepository).findByMemberOfTeamIsNull();
    }

    @Test
    void save() {
        String userName = "second";
        User userToSave = User.builder().id(2L).userName(userName).build();

        when(userRepository.save(any())).thenReturn(userToSave);

        User savedUser = service.save(userToSave);

        assertNotNull(savedUser);
        assertEquals(2L, savedUser.getId());
        assertEquals(userName, savedUser.getUserName());

        verify(userRepository).save(any());
    }

    @Test
    void delete() {
        service.delete(User.builder().build());

        verify(userRepository).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(USER_ID);

        verify(userRepository).deleteById(anyLong());
    }
}