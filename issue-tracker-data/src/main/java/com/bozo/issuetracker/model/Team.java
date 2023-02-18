package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private List<User> members = new ArrayList<>();

    @Column(name = "team_name")
    private String teamName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "assignedTeam")
    private List<Project> projects = new ArrayList<>();

    @Builder
    public Team(Long id, User leader, List<User> members, String teamName, List<Project> projects) {
        super(id);
        this.leader = leader;
        this.members = Optional.ofNullable(members).orElseGet(this::getMembers);
        this.teamName = teamName;
        this.projects = Optional.ofNullable(projects).orElseGet(this::getProjects);
    }
}
