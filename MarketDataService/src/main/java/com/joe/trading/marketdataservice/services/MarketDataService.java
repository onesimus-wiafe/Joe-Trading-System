package com.joe.trading.marketdataservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.model.MarketData;

import java.util.List;
import java.util.Map;

public interface MarketDataService {
    void saveToCache(MarketData marketData);
    void saveToCache(Map<String, MarketData> marketDataMap);
    MarketData getFromCache(String ticker);
    Map<String, MarketData> getAllFromCache();
    void clearCache();
    void updateMarketData(MarketData marketData);
    MarketData getMarketDataFromExchange(String exchange, String ticker);
    void buildInitialCacheEntry() throws JsonProcessingException;
    void initialSubscriptionCheck();
}
