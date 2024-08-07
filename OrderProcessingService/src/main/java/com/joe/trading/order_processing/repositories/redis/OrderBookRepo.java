package com.joe.trading.order_processing.repositories.redis;

import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.repositories.redis.dao.OrderBookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderBookRepo implements OrderBookDAO {

    private final String hashReference = "JOEOrderProcessingOrderBook";

    private final HashOperations<String, String, List<OrderBook>> hashOperations;

    @Autowired
    public OrderBookRepo(RedisTemplate<String, List<OrderBook>> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveOrderBook(String key, List<OrderBook> orderBooks) {
        hashOperations.putIfAbsent(hashReference, key, orderBooks);
    }

    @Override
    public void saveAll(Map<String, List<OrderBook>> orderBooks) {
        hashOperations.putAll(hashReference, orderBooks);
    }

    @Override
    public void updateOrderBook(String key, List<OrderBook> orderBooks) {
        hashOperations.put(hashReference, key, orderBooks);
    }

    @Override
    public void deleteOrderBook(String key) {
        hashOperations.delete(hashReference, key);
    }

    @Override
    public List<OrderBook> getOrderBook(String key) {
        return hashOperations.get(hashReference, key);
    }

    @Override
    public Map<String, List<OrderBook>> getAllOrderBooks() {
        return hashOperations.entries(hashReference);
    }

    @Override
    public void clearCache() {
        this.getAllOrderBooks().forEach(
                (key, data) -> this.deleteOrderBook(key)
        );
    }
}
