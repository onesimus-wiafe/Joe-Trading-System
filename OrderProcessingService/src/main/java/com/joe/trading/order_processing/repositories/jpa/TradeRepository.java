package com.joe.trading.order_processing.repositories.jpa;

import com.joe.trading.order_processing.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Override
    Optional<Trade> findById(Long id);
}
