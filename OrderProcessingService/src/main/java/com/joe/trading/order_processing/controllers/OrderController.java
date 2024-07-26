package com.joe.trading.order_processing.controllers;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.OrderType;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.enums.Ticker;
import com.joe.trading.order_processing.services.OrderService;
import com.joe.trading.order_processing.services.validation.OrderValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderValidationService validationService;
    private final OrderService orderService;

    public OrderController(OrderValidationService validationService, OrderService orderService) {
        this.validationService = validationService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> sendOrder(@RequestBody OrderRequestDTO request){

        // USER EVENT QUEUE

        // Validating Order;
        OrderRequestDTO validatedRequest = validationService.validateOrder(request);

        Order order;
        if (validatedRequest.getIsValidated()) {
            order = buildValidatedOrder(request);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new Order());
        }


        Order response = orderService.saveOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Order buildValidatedOrder(OrderRequestDTO request){
        return new Order(
                Ticker.valueOf(request.getTicker().toUpperCase()),
                request.getQuantity(),
                request.getUnitPrice(),
                Side.valueOf(request.getSide().toUpperCase()),
                AvailableExchanges.valueOf(request.getExchanges().toUpperCase()),
                OrderType.valueOf(request.getOrderType())
        );
    }

    @GetMapping
    public String get(){
        return "HERE!!";
    }
}
