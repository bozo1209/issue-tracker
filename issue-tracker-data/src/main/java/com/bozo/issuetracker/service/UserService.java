package com.bozo.issuetracker.service;

import com.bozo.issuetracker.model.User;

public interface UserService extends CrudService<User, Long>{

    User findByUserName(String name);

    void updateUserInCache(User user);
}
