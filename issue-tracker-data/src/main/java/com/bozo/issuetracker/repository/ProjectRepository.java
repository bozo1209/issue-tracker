package com.bozo.issuetracker.repository;

import com.bozo.issuetracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
