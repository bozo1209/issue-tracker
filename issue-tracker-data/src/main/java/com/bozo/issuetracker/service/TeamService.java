package com.bozo.issuetracker.service;

import com.bozo.issuetracker.model.Team;

import java.util.List;

public interface TeamService extends CrudService<Team, Long> {

    List<Team> findByLeaderIsNull();
}
