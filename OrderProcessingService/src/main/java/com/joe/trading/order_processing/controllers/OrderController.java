package com.joe.trading.order_processing.controllers;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.OrderType;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.enums.Ticker;
import com.joe.trading.order_processing.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> sendOrder(@RequestBody OrderRequestDTO request){

        System.out.println(request);

        // OrderRequest should also have a field for portfolio Id.
        Order order = new Order(
                Ticker.valueOf(request.getTicker().toUpperCase()),
                request.getQuantity(),
                request.getUnitPrice(),
                Side.valueOf(request.getSide().toUpperCase()),
                AvailableExchanges.valueOf(request.getExchanges().toUpperCase()),
                OrderType.valueOf(request.getOrderType())
        );
        Order ord = orderService.saveOrder(order);

        System.out.println("---!!!!-----!!!!---");
        System.out.println("New Order made to the exchange");
        System.out.println(ord);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public String get(){
        return "HERE!!";
    }
}
