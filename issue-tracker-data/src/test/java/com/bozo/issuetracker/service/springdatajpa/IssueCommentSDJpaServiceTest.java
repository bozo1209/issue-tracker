package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.repository.IssueCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IssueCommentSDJpaServiceTest {

    public static final String COMMENT = "comment";
    public static final Long COMMENT_ID = 1L;

    @Mock
    IssueCommentRepository issueCommentRepository;

    @InjectMocks
    IssueCommentSDJpaService service;

    IssueComment returnedComment;

    @BeforeEach
    void setUp() {
        returnedComment = IssueComment.builder().id(COMMENT_ID).comment(COMMENT).build();
    }

    @Test
    void findAll() {
        List<IssueComment> returnedCommentList = List.of(returnedComment);

        when(issueCommentRepository.findAll()).thenReturn(returnedCommentList);

        List<IssueComment> issueCommentList = service.findAll();

        assertNotNull(returnedCommentList);
        assertEquals(1, issueCommentList.size());

        verify(issueCommentRepository).findAll();
    }

    @Test
    void findById() {
        when(issueCommentRepository.findById(anyLong())).thenReturn(Optional.of(returnedComment));

        IssueComment comment = service.findById(COMMENT_ID);

        assertNotNull(comment);
        assertEquals(returnedComment, comment);
        assertEquals(returnedComment.getId(), comment.getId());

        verify(issueCommentRepository).findById(anyLong());
    }

    @Test
    void findByIdNotFound() {
        when(issueCommentRepository.findById(anyLong())).thenReturn(Optional.empty());

        IssueComment comment = service.findById(COMMENT_ID);

        assertNull(comment.getId());

        verify(issueCommentRepository).findById(anyLong());
    }

    @Test
    void save() {
        String comment = "comment 2";
        IssueComment issueCommentToSave = IssueComment.builder().id(2L).comment(comment).build();

        when(issueCommentRepository.save(any())).thenReturn(issueCommentToSave);

        IssueComment savedComment = service.save(issueCommentToSave);

        assertNotNull(savedComment);
        assertEquals(comment, savedComment.getComment());

        verify(issueCommentRepository).save(any());
    }

    @Test
    void delete() {
        service.delete(IssueComment.builder().build());

        verify(issueCommentRepository).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(COMMENT_ID);

        verify(issueCommentRepository).deleteById(anyLong());
    }
}