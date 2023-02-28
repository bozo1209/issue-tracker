package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team extends BaseEntity{

    @OneToOne(mappedBy = "leaderOfTeam")
    private User leader;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "memberOfTeam")
    private Set<User> members = new HashSet<>();

    @Column(name = "team_name")
    private String teamName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "assignedTeam")
    private Set<Project> projects = new HashSet<>();

    @Builder
    public Team(Long id, User leader, Set<User> members, String teamName, Set<Project> projects) {
        super(id);
        this.leader = leader;
        this.members = Optional.ofNullable(members).orElseGet(this::getMembers);
        this.teamName = teamName;
        this.projects = Optional.ofNullable(projects).orElseGet(this::getProjects);
    }
}
