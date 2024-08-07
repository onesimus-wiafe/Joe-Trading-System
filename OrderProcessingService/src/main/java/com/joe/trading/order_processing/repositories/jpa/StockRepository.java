package com.joe.trading.order_processing.repositories.jpa;

import com.joe.trading.order_processing.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Override
    Optional<Stock> findById(Long id);
}
