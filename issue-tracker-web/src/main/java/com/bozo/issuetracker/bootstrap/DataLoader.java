package com.bozo.issuetracker.bootstrap;

import com.bozo.issuetracker.config.PasswordConfig;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.*;
import com.bozo.issuetracker.service.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final TeamService teamService;
    private final PasswordConfig passwordConfig;

    @Override
    public void run(String... args) throws Exception {
        loadDate();
    }

    private void loadDate(){
        User user1 = User.builder().userName("user1").password(passwordConfig.passwordEncoder().encode("user1")).role(UserRoles.ADMIN).build();

        Issue issue1 = Issue.builder().description("issue 1").issueCreator(user1).build();

        user1.getIssuesCreated().add(issue1);
        user1.getIssuesObserve().add(issue1);

        User user2 = User.builder().userName("user2").password(passwordConfig.passwordEncoder().encode("user2")).role(UserRoles.USER).build();
        Issue issue2 = Issue.builder().description("issue 2").issueCreator(user2).build();
        user2.getIssuesCreated().add(issue2);
        user2.getIssuesObserve().add(issue2);

        User user3 = User.builder().userName("user3").password(passwordConfig.passwordEncoder().encode("user3")).role(UserRoles.TEAM_LEADER).build();

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);
        User savedUser3 = userService.save(user3);

        savedUser1.getIssuesObserve().add(issue2);

        IssueComment comment1 = IssueComment.builder().comment("comment 1").commentCreator(savedUser2).issue(savedUser1.getIssuesCreated().get(0)).build();
        savedUser2.getCommentsCreated().add(comment1);

        IssueComment comment2 = IssueComment.builder().comment("comment 2").commentCreator(savedUser1).issue(savedUser1.getIssuesCreated().get(0)).build();
        savedUser1.getCommentsCreated().add(comment2);

        IssueComment comment3 = IssueComment.builder().comment("comment 3").commentCreator(savedUser2).issue(savedUser1.getIssuesCreated().get(0)).build();
        savedUser2.getCommentsCreated().add(comment3);

        IssueComment comment4 = IssueComment.builder().comment("comment 4").commentCreator(savedUser1).issue(savedUser1.getIssuesCreated().get(0)).build();
        savedUser1.getCommentsCreated().add(comment4);

        Team team = Team.builder().teamName("team 1").build();
        Team team2 = Team.builder().teamName("team 2").build();

        Team savedTeam = teamService.save(team);
        Team savedTeam2 = teamService.save(team2);

        savedTeam.setLeader(savedUser1);
        savedTeam.setMembers(Set.of(savedUser1, savedUser2));

        savedTeam2.setLeader(savedUser3);
        savedTeam2.setMembers(Set.of(savedUser3));

        savedUser1.setLeaderOfTeam(savedTeam);
        savedUser1.setMemberOfTeam(savedTeam);

        savedUser2.setMemberOfTeam(savedTeam);

        savedUser3.setLeaderOfTeam(savedTeam2);
        savedUser3.setMemberOfTeam(savedTeam2);

        Project project = Project.builder()
                .projectName("project 1")
                .assignedTeam(savedTeam)
                .issues(Set.of(
                        savedUser1.getIssuesCreated().get(0),
                        savedUser2.getIssuesCreated().get(0)))
                .build();

        savedUser1.getIssuesCreated().get(0).setProject(project);
        savedUser2.getIssuesCreated().get(0).setProject(project);

        Project project2 = Project.builder().projectName("project 2").assignedTeam(savedTeam2).build();

        savedTeam.setProjects(Set.of(project));

        savedTeam2.setProjects(Set.of(project2));

        teamService.save(savedTeam);
        teamService.save(savedTeam2);
    }
}
