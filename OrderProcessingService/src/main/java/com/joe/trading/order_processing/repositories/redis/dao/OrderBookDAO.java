package com.joe.trading.order_processing.repositories.redis.dao;


import com.joe.trading.order_processing.entities.OrderBook;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderBookDAO {
    void saveOrderBook(String key, List<OrderBook> orderBooks);
    void saveAll(Map<String, List<OrderBook>> orderBooks);
    void updateOrderBook(String key, List<OrderBook> orderBooks);
    void deleteOrderBook(String key);
    Optional<List<OrderBook>> getOrderBook(String key);
    Map<String, List<OrderBook>> getAllOrderBooks();
    void clearCache();
}
