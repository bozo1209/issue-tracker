package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserSDJpaService implements UserService {

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(Long aLong) {
        return null;
    }

    @Override
    public User findByName(String name) {
        return null;
    }

    @Override
    public User save(User object) {
        return null;
    }

    @Override
    public void delete(User object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

}
