package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    OrderResponseDTO saveOrder(Order order, Long portfolioId);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO cancelOrder(Long orderId, Long userId);

    Page<OrderResponseDTO> getAllOrdersPerUserId(Long userId, OrderRequestDTO request);
}
