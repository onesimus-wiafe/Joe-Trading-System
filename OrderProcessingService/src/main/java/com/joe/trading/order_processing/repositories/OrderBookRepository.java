package com.joe.trading.order_processing.repositories;

import com.joe.trading.order_processing.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderBookRepository extends JpaRepository<OrderBook, String> {

    @Override
    Optional<OrderBook> findById(String id);
}
