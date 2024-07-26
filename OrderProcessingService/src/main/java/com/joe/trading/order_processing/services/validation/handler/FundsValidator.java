package com.joe.trading.order_processing.services.validation.handler;

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;

public class FundsValidator extends ValidationHandler{

    private User user;

    public FundsValidator(User user){
        super();
        this.user = user;
    }

    @Override
    public OrderRequestDTO validate(OrderRequestDTO orderRequestDTO) {

        return super.validate(validateFunds(orderRequestDTO));
    }

    private OrderRequestDTO validateFunds(OrderRequestDTO orderRequestDTO){

        if (this.user == null) {
            super.setNext(null);
            orderRequestDTO.setIsValidated(Boolean.FALSE);
        }
        else {
            double expectedTotal = orderRequestDTO.getUnitPrice() * orderRequestDTO.getQuantity();

            if ((expectedTotal <= this.user.getTotalFunds())) {
                orderRequestDTO.setIsValidated(Boolean.TRUE);
            } else {
                super.setNext(null);
                orderRequestDTO.setIsValidated(Boolean.FALSE);
            }
        }
        return orderRequestDTO;
    }
}
