package com.joe.trading.user_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joe.trading.user_management.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
