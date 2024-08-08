package com.trading.joe.reportservice.repository;

import com.trading.joe.reportservice.entities.MarketData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarketDataRepository extends MongoRepository<MarketData,String> {
}
