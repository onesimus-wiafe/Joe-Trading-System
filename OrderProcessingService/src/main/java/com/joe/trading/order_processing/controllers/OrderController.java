package com.joe.trading.order_processing.controllers;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.dto.OrderResponseDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.OrderType;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.enums.Ticker;
import com.joe.trading.order_processing.services.OrderService;
import com.joe.trading.order_processing.services.validation.OrderValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderValidationService validationService;
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderValidationService validationService, OrderService orderService) {
        this.validationService = validationService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> sendOrder(@RequestBody OrderRequestDTO request){
        OrderResponseDTO response = new OrderResponseDTO();

        OrderRequestDTO validatedRequest = validationService.validateOrder(request);

        Order order;
        if (validatedRequest.getIsValidated()) {
            order = buildValidatedOrder(request);
        }
        else {
            response.setMessage("Order Validation Did Not Pass");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
        }

        response = orderService.saveOrder(order);

        response.setMessage("New Order Created");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrdersByUserId(OrderRequestDTO request){

        return ResponseEntity.ok(orderService.getAllOrdersPerUserId(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> cancelAnOrder(@PathVariable Long id){
        return ResponseEntity.ok(orderService.cancelOrder(id));
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
}
