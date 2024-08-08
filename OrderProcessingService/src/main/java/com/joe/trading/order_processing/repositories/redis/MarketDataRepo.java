package com.joe.trading.order_processing.repositories.redis;

import com.joe.trading.order_processing.entities.cache.MarketData;
import com.joe.trading.order_processing.repositories.redis.dao.MarketDataDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class MarketDataRepo implements MarketDataDAO {
    private final String hashReference = "JOEOrderProcessingMarketData";

    private final HashOperations<String, String, MarketData> hashOperations;

    @Autowired
    public MarketDataRepo(RedisTemplate<String, MarketData> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveMarketData(MarketData data, String ticker) {
        hashOperations.putIfAbsent(hashReference, ticker, data);
    }

    @Override
    public void saveAll(Map<String, MarketData> marketDataMap) {
        hashOperations.putAll(hashReference, marketDataMap);
    }

    @Override
    public void updateMarketData(MarketData data) {
        hashOperations.put(hashReference, data.getTICKER(), data);
    }

    @Override
    public void deleteMarketData(String ticker) {
        hashOperations.delete(hashReference, ticker);
    }

    @Override
    public void clearCache() {
        this.getAll().forEach(
                (key, data) -> this.deleteMarketData(key)
        );
    }

    @Override
    public Optional<MarketData> getMarketData(String ticker) {
        return Optional.of(hashOperations.get(hashReference, ticker));
    }

    @Override
    public Map<String, MarketData> getAll() {
        return hashOperations.entries(hashReference);
    }
}
