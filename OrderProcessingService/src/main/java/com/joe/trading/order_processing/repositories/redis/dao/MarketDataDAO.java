package com.joe.trading.order_processing.repositories.redis.dao;

import com.joe.trading.order_processing.entities.cache.MarketData;

import java.util.Map;
import java.util.Optional;

public interface MarketDataDAO {
    void saveMarketData(MarketData data, String ticker);
    void saveAll(Map<String, MarketData> marketDataMap);
    void updateMarketData(MarketData data);
    void deleteMarketData(String ticker);
    void clearCache();
    Optional<MarketData> getMarketData(String ticker);
    Map<String, MarketData> getAll();
}
