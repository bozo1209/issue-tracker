package com.bozo.issuetracker.service.springdatajpa;

import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.repository.UserRepository;
import com.bozo.issuetracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserSDJpaService implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseGet(User::new);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name).orElseGet(User::new);
    }

    @Override
    public User save(User object) {
        return userRepository.save(object);
    }

    @Override
    public void delete(User object) {
        userRepository.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
