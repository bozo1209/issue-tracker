package com.bozo.issuetracker.repository;

import com.bozo.issuetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
