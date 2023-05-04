package com.bozo.issuetracker.repository;

import com.bozo.issuetracker.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByLeaderIdOrLeaderIsNull(Long leaderId);
}
