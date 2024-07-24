package com.joe.trading.user_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joe.trading.user_management.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
