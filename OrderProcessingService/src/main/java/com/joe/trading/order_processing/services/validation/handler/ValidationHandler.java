package com.joe.trading.order_processing.services.validation.handler;

import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;

public abstract class ValidationHandler {
    private ValidationHandler handler;

    public ValidationHandler() {}

    public void setNext(ValidationHandler handler) {
        this.handler = handler;
    }

    public OrderRequestDTO validate(OrderRequestDTO orderRequestDTO){
        return (handler == null) ? orderRequestDTO : handler.validate(orderRequestDTO);
    }
}
