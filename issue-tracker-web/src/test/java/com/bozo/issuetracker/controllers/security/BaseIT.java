package com.bozo.issuetracker.controllers.security;

import com.bozo.issuetracker.repository.*;
import com.bozo.issuetracker.service.springdatajpa.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIT {

    @Autowired
    WebApplicationContext webApplicationContext;

//    @MockBean
    @Autowired
    UserRepository userRepository;

//    @MockBean
    @Autowired
    TeamRepository teamRepository;

//    @MockBean
    @Autowired
    ProjectRepository projectRepository;

//    @MockBean
    @Autowired
    IssueRepository issueRepository;

//    @MockBean
    @Autowired
    IssueCommentRepository issueCommentRepository;

//    @MockBean
    @Autowired
    UserSDJpaService userService;

//    @MockBean
    @Autowired
    TeamSDJpaService teamService;

//    @MockBean
    @Autowired
    ProjectSDJpaService projectService;

//    @MockBean
    @Autowired
    IssueSDJpaService issueService;

//    @MockBean
    @Autowired
    IssueCommentSDJpaService commentService;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
}
