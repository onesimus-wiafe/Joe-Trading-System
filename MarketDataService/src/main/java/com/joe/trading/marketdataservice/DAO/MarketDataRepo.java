package com.joe.trading.marketdataservice.DAO;

import com.joe.trading.marketdataservice.model.MarketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MarketDataRepo implements MarketDataDAO{
    private final String hashReference = "JOEMarketData";

    private final HashOperations<String, String, MarketData> hashOperations;

    @Autowired
    public MarketDataRepo(RedisTemplate<String, MarketData> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveMarketData(MarketData data, String ticker) {
        this.hashOperations.putIfAbsent(hashReference, ticker, data);
    }

    @Override
    public void saveAll(Map<String, MarketData> marketDataMap) {
        this.hashOperations.putAll(hashReference, marketDataMap);
    }

    @Override
    public void updateMarketData(MarketData data) {
        this.hashOperations.put(hashReference, data.getTICKER(), data);
    }

    @Override
    public void deleteMarketData(String ticker) {
        this.hashOperations.delete(hashReference, ticker);
    }

    @Override
    public MarketData getMarketData(String ticker) {
        return this.hashOperations.get(hashReference, ticker);
    }

    @Override
    public Map<String, MarketData> getAll() {
        return this.hashOperations.entries(hashReference);
    }

    @Override
    public void clearCache(){
        this.getAll().forEach((ticker, data) -> this.deleteMarketData(ticker));
    }
}
