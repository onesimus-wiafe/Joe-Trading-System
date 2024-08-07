package com.joe.trading.order_processing.services.validation.handler;

import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;

import java.util.List;

public class OwnershipValidator extends ValidationHandler {

    private User user;

    public OwnershipValidator(User user) {
        super();
        this.user = user;
    }

    @Override
    public OrderRequestDTO validate(OrderRequestDTO orderRequestDTO) {
        return super.validate(userIsOwner(orderRequestDTO));
    }

    private OrderRequestDTO userIsOwner(OrderRequestDTO request){
        List<Portfolio> portfolios = this.user.getPortfolios();

        if (portfolios.isEmpty()){
            super.setNext(null);
            request.setIsValidated(Boolean.FALSE);
            return request;
        }

        Portfolio portfolio = portfolios.stream().filter(p -> p.getId().equals(request.getPortfolioId()))
                .findFirst().orElse(null);

        if (portfolio == null) {
            super.setNext(null);
            request.setIsValidated(Boolean.FALSE);
        }
        else {
            request.setIsValidated(Boolean.TRUE);
        }
        return request;
    };
}
