package com.joe.trading.order_processing.services.validation;

import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;

public interface OrderValidationService {
    OrderRequestDTO validateOrder(OrderRequestDTO orderRequest);
}
