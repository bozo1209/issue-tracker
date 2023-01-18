package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.repository.IssueRepository;
import com.bozo.issuetracker.service.IssueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IssueSDJpaService implements IssueService {

    private final IssueRepository issueRepository;

    @Override
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    @Override
    public Issue findById(Long id) {
        return issueRepository.findById(id).orElseGet(Issue::new);
    }

    @Override
    public Issue save(Issue object) {
        return issueRepository.save(object);
    }

    @Override
    public void delete(Issue object) {
        issueRepository.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        issueRepository.deleteById(id);
    }
}
