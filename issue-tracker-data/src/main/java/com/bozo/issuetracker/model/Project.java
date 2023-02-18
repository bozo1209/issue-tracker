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
@Table(name = "project")
public class Project extends BaseEntity{

    @Column(name = "project_name")
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "assigned_team_id")
    private Team assignedTeam;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private List<Issue> issues = new ArrayList<>();

    @Builder
    public Project(Long id, String projectName, Team assignedTeam, List<Issue> issues) {
        super(id);
        this.projectName = projectName;
        this.assignedTeam = assignedTeam;
        this.issues = Optional.ofNullable(issues).orElseGet(this::getIssues);
    }
}
