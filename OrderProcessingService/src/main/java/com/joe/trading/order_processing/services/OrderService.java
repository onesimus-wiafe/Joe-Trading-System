package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public OrderResponseDTO saveOrder(Order order);
    public List<OrderResponseDTO> getAllOrders();
    public OrderResponseDTO cancelOrder(Long id);
    public Page<OrderResponseDTO> getAllOrdersPerUserId(OrderRequestDTO request);
}
