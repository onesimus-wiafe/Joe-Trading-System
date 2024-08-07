package com.joe.trading.order_processing.repositories.redis;

import com.joe.trading.order_processing.repositories.redis.dao.InternalOpenOrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InternalOpenOrderRepo implements InternalOpenOrderDAO {
    private final String hashReference = "JOEOrderProcessingInternalOrders";

    private final HashOperations<String, String,List<String>> hashOperations;

    @Autowired
    public InternalOpenOrderRepo(RedisTemplate<String, List<String>> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveInternalOrder(String key, List<String> orders) {
        hashOperations.putIfAbsent(hashReference, key, orders);
    }

    @Override
    public void updateInternalOrder(String key, List<String> orders) {
        hashOperations.put(hashReference, key, orders);
    }

    @Override
    public void deleteInternalOrder(String key) {
        hashOperations.delete(hashReference, key);
    }

    @Override
    public List<String> getInternalOrder(String key) {
        return hashOperations.get(hashReference, key);
    }

    @Override
    public Map<String, List<String>> getAllInternalOrders() {
        return hashOperations.entries(hashReference);
    }

    @Override
    public void clearCache() {
        this.getAllInternalOrders().forEach(
                (key, data) -> this.deleteInternalOrder(key)
        );
    }
}
