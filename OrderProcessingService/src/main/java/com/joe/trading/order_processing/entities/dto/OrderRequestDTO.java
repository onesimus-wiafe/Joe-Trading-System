package com.joe.trading.order_processing.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private String ticker;
    private Integer quantity;
    private Double unitPrice;
    private String side;
    private String exchanges = "exchange1";
    private String orderType;

    @Override
    public String toString() {
        return "OrderRequestDTO{" +
                "ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", side='" + side + '\'' +
                ", exchanges='" + exchanges + '\'' +
                ", type='" + orderType + '\'' +
                '}';
    }
}
