package com.joe.trading.order_processing.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductOrder {
    private String product;
    private Integer quantity;
    private Double price;
    private String side;
    private String type;
}
