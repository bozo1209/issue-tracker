package com.bozo.issuetracker.repository;

import com.bozo.issuetracker.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
