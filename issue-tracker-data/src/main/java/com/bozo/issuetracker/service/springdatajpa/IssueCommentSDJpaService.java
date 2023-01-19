package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.repository.IssueCommentRepository;
import com.bozo.issuetracker.service.IssueCommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IssueCommentSDJpaService implements IssueCommentService {

    private final IssueCommentRepository issueCommentRepository;

    @Override
    public List<IssueComment> findAll() {
        return issueCommentRepository.findAll();
    }

    @Override
    public IssueComment findById(Long id) {
        return issueCommentRepository.findById(id).orElseGet(IssueComment::new);
    }

    @Override
    public IssueComment save(IssueComment object) {
        return issueCommentRepository.save(object);
    }

    @Override
    public void delete(IssueComment object) {
        issueCommentRepository.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        issueCommentRepository.deleteById(id);
    }
}
