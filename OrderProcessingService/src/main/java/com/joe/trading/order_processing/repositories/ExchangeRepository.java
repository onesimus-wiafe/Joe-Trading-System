package com.joe.trading.order_processing.repositories;

import com.joe.trading.order_processing.entities.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, String> {
    @Override
    Optional<Exchange> findById(String url);

    @Override
    <S extends Exchange> List<S> saveAll(Iterable<S> entities);
}
