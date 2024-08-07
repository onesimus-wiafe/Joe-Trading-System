package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.OrderBook;

import java.util.List;

public interface OrderBookService {
    void updateOrderBooks(List<OrderBook> orderBooks);
}
