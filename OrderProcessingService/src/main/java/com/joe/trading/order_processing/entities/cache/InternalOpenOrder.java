package com.joe.trading.order_processing.entities.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InternalOpenOrder {
    private String orderId;
    private Integer quantity;
    private Integer totalExecutedQuantity;
}
