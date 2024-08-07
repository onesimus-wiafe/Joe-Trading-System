package com.trading.joe.reportservice.repository;

import com.trading.joe.reportservice.entities.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioRepository extends MongoRepository<Portfolio,Long> {

}
