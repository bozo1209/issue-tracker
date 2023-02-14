package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<Issue> issues;
}
