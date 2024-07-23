package com.joe.trading.user_management.repository;

import com.joe.trading.user_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
