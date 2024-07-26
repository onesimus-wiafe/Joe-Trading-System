package com.joe.trading.order_processing.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO {
    private String ticker;
    private Integer quantity;
    private Double unitPrice;
    private String Side;
    private String Strategy;
    private String orderType;
    private String message;

    // TODO: may add a Trades DTO to the response in the future.
}
