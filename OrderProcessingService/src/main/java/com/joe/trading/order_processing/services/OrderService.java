package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;

public interface OrderService {

    public Order saveOrder(Order order);
}
