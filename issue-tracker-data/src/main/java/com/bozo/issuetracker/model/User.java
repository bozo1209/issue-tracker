package com.bozo.issuetracker.model;

import com.bozo.issuetracker.enums.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User extends BaseEntity {

    @Column(name = "user_name", unique = true)
    private String userName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "issueCreator")
    private List<Issue> issuesCreated = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_issue_observe",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "issue_id"))
    private Set<Issue> issuesObserve = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "commentCreator")
    private List<IssueComment> commentsCreated = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "app_user_team_leader",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "leader_of_team_id"))
    private Team leaderOfTeam;

    @ManyToOne
    @JoinColumn(name = "member_of_team_id")
    private Team memberOfTeam;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRoles role;

    @Size(min = 5)
    @Column(name = "password", nullable = false)
    private String password;

    @Builder
    public User(Long id,
                String userName,
                List<Issue> issuesCreated,
                Set<Issue> issuesObserve,
                List<IssueComment> commentsCreated,
                Team leaderOfTeam,
                Team memberOfTeam,
                UserRoles role,
                String password) {
        super(id);
        this.userName = userName;
        this.issuesCreated = Optional.ofNullable(issuesCreated).orElseGet(this::getIssuesCreated);
        this.issuesObserve = Optional.ofNullable(issuesObserve).orElseGet(this::getIssuesObserve);
        this.commentsCreated = Optional.ofNullable(commentsCreated).orElseGet(this::getCommentsCreated);
        this.leaderOfTeam = leaderOfTeam;
        this.memberOfTeam = memberOfTeam;
        this.role = role;
        this.password = password;
    }
}
