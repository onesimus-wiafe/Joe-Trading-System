package com.joe.trading.order_processing.services.validation;

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.repositories.UserRepository;
import com.joe.trading.order_processing.services.validation.handler.*;
import org.springframework.stereotype.Service;

@Service
public class OrderValidationServiceImpl implements OrderValidationService{

    private final UserRepository userRepository;

    public OrderValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OrderRequestDTO validateOrder(OrderRequestDTO orderRequest) {
        orderRequest.setSide(orderRequest.getSide().toUpperCase());

        User user = userRepository.findById(orderRequest.getUserId()).orElse(null);

        return switch (Side.valueOf(orderRequest.getSide())){
            case BUY -> {
                ValidationHandler handler = new FundsValidator(user);
                handler.setNext(new QuantityValidator(Side.BUY));
                handler.setNext(new PriceValidator(Side.BUY));
                yield handler.validate(orderRequest);
            }
            case SELL -> {
                ValidationHandler handler = new OwnershipValidator(user);
                handler.setNext(new QuantityValidator(Side.SELL));
                handler.setNext(new PriceValidator(Side.SELL));
                yield handler.validate(orderRequest);
            }
        };
    }
}
