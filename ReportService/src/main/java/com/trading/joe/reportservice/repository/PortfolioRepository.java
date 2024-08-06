package com.trading.joe.reportservice.repository;

import com.trading.joe.reportservice.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

}
