package com.trading.joe.reportservice.repository;

import com.trading.joe.reportservice.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {

}
