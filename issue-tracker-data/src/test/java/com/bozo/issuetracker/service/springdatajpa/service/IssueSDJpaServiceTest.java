package com.bozo.issuetracker.service.springdatajpa.service;

import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.repository.IssueRepository;
import com.bozo.issuetracker.service.springdatajpa.IssueSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueSDJpaServiceTest {

    public static final String ISSUE_DESCRIPTION = "issue 1";
    public static final Long ISSUE_ID = 1L;

    @Mock
    IssueRepository issueRepository;

    @InjectMocks
    IssueSDJpaService service;

    Issue returnedIssue;

    @BeforeEach
    void setUp() {
        returnedIssue = Issue.builder().id(ISSUE_ID).description(ISSUE_DESCRIPTION).build();
    }

    @Test
    void findAll() {
        List<Issue> returnedIssueList = List.of(returnedIssue);

        when(issueRepository.findAll()).thenReturn(returnedIssueList);

        List<Issue> issueList = service.findAll();

        assertNotNull(issueList);
        assertEquals(1, issueList.size());

        verify(issueRepository).findAll();
    }

    @Test
    void findById() {
        when(issueRepository.findById(anyLong())).thenReturn(Optional.of(returnedIssue));

        Issue issue = service.findById(ISSUE_ID);

        assertNotNull(issue);
        assertEquals(returnedIssue, issue);
        assertEquals(returnedIssue.getId(), issue.getId());

        verify(issueRepository).findById(anyLong());
    }

    @Test
    void findByIdNotFound() {
        when(issueRepository.findById(anyLong())).thenReturn(Optional.empty());

        Issue issue = service.findById(ISSUE_ID);

        assertNull(issue.getId());

        verify(issueRepository).findById(anyLong());
    }

    @Test
    void save() {
        String description = "issue 2";
        Issue issueToSave = Issue.builder().id(2L).description(description).build();

        when(issueRepository.save(any())).thenReturn(issueToSave);

        Issue savedIssue = service.save(issueToSave);

        assertNotNull(savedIssue);
        assertEquals(description, savedIssue.getDescription());

        verify(issueRepository).save(any());
    }

    @Test
    void delete() {
        service.delete(Issue.builder().build());

        verify(issueRepository).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(ISSUE_ID);

        verify(issueRepository).deleteById(anyLong());
    }
}