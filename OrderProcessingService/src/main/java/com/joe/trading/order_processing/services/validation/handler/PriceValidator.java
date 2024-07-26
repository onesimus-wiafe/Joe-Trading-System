package com.joe.trading.order_processing.services.validation.handler;

import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.Side;

public class PriceValidator extends ValidationHandler {
    private Side side;

    public PriceValidator(Side side){
        super();
        this.side = side;
    }

    @Override
    public OrderRequestDTO validate(OrderRequestDTO orderRequestDTO) {
        return null;
    }
}
