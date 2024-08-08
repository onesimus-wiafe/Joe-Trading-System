package com.joe.trading.order_processing.controllers;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.dto.OrderResponseDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.OrderType;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.enums.Ticker;
import com.joe.trading.order_processing.services.OrderService;
import com.joe.trading.order_processing.services.validation.OrderValidationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderValidationService validationService;
    private final OrderService orderService;

    public OrderController(OrderValidationService validationService, OrderService orderService) {
        this.validationService = validationService;
        this.orderService = orderService;
    }

    private User authenticate(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDTO> sendOrder(@RequestBody OrderRequestDTO request) {

        var principal = authenticate();

        OrderResponseDTO response = new OrderResponseDTO();

        // Validating Order
        OrderRequestDTO validatedRequest = validationService.validateOrder(principal.getId(), request);

        Order order;
        if (validatedRequest.getIsValidated()) {
            order = buildValidatedOrder(request, principal.getId());
        } else {
            response.setMessage("Order Validation Did Not Pass");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
        }

        response = orderService.saveOrder(order, request.getPortfolioId());

        response.setMessage("New Order Created");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrdersByUserId(
            @PathVariable("id") Long userId, OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.getAllOrdersPerUserId(userId, request));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> cancelAnOrder(@PathVariable Long orderId) {
        var principal = authenticate();

        return ResponseEntity.ok(orderService.cancelOrder(orderId, principal.getId()));
    }

    private Order buildValidatedOrder(OrderRequestDTO request, Long userId) {
        Order order = new Order(
                Ticker.valueOf(request.getTicker().toUpperCase()),
                request.getQuantity(),
                request.getUnitPrice(),
                Side.valueOf(request.getSide().toUpperCase()),
                AvailableExchanges.valueOf(request.getExchanges().toUpperCase()),
                OrderType.valueOf(request.getOrderType()));

        order.setUserId(userId);

        return order;
    }
}
