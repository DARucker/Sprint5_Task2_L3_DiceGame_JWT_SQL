package com.sprint5.task2.fase3.sql.repository;

import com.sprint5.task2.fase3.sql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
