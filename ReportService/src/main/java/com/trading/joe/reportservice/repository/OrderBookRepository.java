package com.trading.joe.reportservice.repository;

import com.trading.joe.reportservice.entities.OrderBooks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderBookRepository extends MongoRepository<OrderBooks,String> {
}
