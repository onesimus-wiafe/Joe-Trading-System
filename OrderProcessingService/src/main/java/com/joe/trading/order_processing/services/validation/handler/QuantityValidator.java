package com.joe.trading.order_processing.services.validation.handler;

import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.Side;

public class QuantityValidator extends ValidationHandler{

    private Side side;

    public QuantityValidator(Side side){
        super();
        this.side = side;
    }

    @Override
    public OrderRequestDTO validate(OrderRequestDTO orderRequestDTO) {
        return null;
    }
}
