package com.joe.trading.order_processing.repositories.cache;

import com.joe.trading.order_processing.entities.cache.InternalOpenOrder;
import com.joe.trading.order_processing.repositories.dao.InternalOpenOrderDAO;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InternalOpenOrderRepo implements InternalOpenOrderDAO {
    private final String hashReference = "JOEOrderProcessingInternalOrders";

    private final HashOperations<String, String, List<InternalOpenOrder>> hashOperations;

    public InternalOpenOrderRepo(RedisTemplate<String, List<InternalOpenOrder>> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveInternalOrder(String key, List<InternalOpenOrder> orders) {
        hashOperations.putIfAbsent(hashReference, key, orders);
    }

    @Override
    public void updateInternalOrder(String key, List<InternalOpenOrder> orders) {
        hashOperations.put(hashReference, key, orders);
    }

    @Override
    public void deleteInternalOrder(String key) {
        hashOperations.delete(hashReference, key);
    }

    @Override
    public List<InternalOpenOrder> getInternalOrder(String key) {
        return hashOperations.get(hashReference, key);
    }

    @Override
    public Map<String, List<InternalOpenOrder>> getAllInternalOrders() {
        return hashOperations.entries(hashReference);
    }

    @Override
    public void clearCache() {
        this.getAllInternalOrders().forEach(
                (key, data) -> this.deleteInternalOrder(key)
        );
    }
}
