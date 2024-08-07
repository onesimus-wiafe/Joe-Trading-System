package com.joe.trading.order_processing.repositories.jpa;

import com.joe.trading.order_processing.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Override
    Optional<Portfolio> findById(Long id);
}
