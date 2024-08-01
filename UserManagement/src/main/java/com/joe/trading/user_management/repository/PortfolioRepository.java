package com.joe.trading.user_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joe.trading.user_management.entities.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserId(Long userId);
}
