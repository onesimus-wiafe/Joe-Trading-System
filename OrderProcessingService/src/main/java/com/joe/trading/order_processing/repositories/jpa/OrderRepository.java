package com.joe.trading.order_processing.repositories.jpa;

import com.joe.trading.order_processing.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    Optional<Order> findById(Long id);
}
