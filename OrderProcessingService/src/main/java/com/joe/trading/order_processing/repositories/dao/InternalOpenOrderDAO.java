package com.joe.trading.order_processing.repositories.dao;

import com.joe.trading.order_processing.entities.cache.InternalOpenOrder;

import java.util.List;
import java.util.Map;

public interface InternalOpenOrderDAO {

    void saveInternalOrder(String key, List<InternalOpenOrder> orders);
    void updateInternalOrder(String key, List<InternalOpenOrder> orders);
    void deleteInternalOrder(String key);
    List<InternalOpenOrder> getInternalOrder(String key);
    Map<String, List<InternalOpenOrder>> getAllInternalOrders();
    void clearCache();
}
