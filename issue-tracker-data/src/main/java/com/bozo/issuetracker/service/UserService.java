package com.bozo.issuetracker.service;

import com.bozo.issuetracker.model.User;

import java.util.List;

public interface UserService extends CrudService<User, Long>{

    User findByUserName(String name);

    List<User> findByMemberOfTeamIsNull();

    void updateUserInCache(User user);
}
