package com.joe.trading.marketdataservice.DAO;

import com.joe.trading.marketdataservice.model.MarketData;

import java.util.Map;

public interface MarketDataDAO {

    void saveMarketData(MarketData data, String ticker);
    void saveAll(Map<String, MarketData> marketDataMap);
    void updateMarketData(MarketData data);
    void deleteMarketData(String ticker);
    void clearCache();
    MarketData getMarketData(String ticker);
    Map<String, MarketData> getAll();
}
