package com.joe.trading.order_processing.repositories.redis.dao;

import java.util.List;
import java.util.Map;

public interface InternalOpenOrderDAO {

    void saveInternalOrder(String key, List<String> orders);
    void updateInternalOrder(String key, List<String> orders);
    void deleteInternalOrder(String key);
    List<String> getInternalOrder(String key);
    Map<String, List<String>> getAllInternalOrders();
    void clearCache();
}
