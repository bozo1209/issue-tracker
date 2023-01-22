package com.bozo.issuetracker.bootstrap;

import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.IssueCommentService;
import com.bozo.issuetracker.service.IssueService;
import com.bozo.issuetracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;

    @Override
    public void run(String... args) throws Exception {
        loadDate();
    }

    private void loadDate(){
        User user1 = User.builder().userName("user1").build();

        Issue issue1 = Issue.builder().description("issue 1").issueCreator(user1).build();

        user1.getIssuesCreated().add(issue1);
        user1.getIssuesObserve().add(issue1);

        User user2 = User.builder().userName("user2").build();
        Issue issue2 = Issue.builder().description("issue 2").issueCreator(user2).build();
        user2.getIssuesCreated().add(issue2);
        user2.getIssuesObserve().add(issue2);

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);

        savedUser1.getIssuesObserve().add(issue2);

        userService.save(savedUser1);
    }
}
