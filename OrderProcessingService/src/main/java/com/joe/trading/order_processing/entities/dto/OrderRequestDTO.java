package com.joe.trading.order_processing.entities.dto;

import jakarta.validation.constraints.Min;
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
    private Long portfolioId;
    private Integer quantity;
    private Double unitPrice;
    private String side;
    private String exchanges = "all";
    private String orderType;
    private Boolean isValidated = false;

    @Min(value = 1, message = "Page number must be greater than 0")
    private int page = 1;

    @Min(value = 1, message = "Page size must be greater than 0")
    private int size = 20;
    private String sortBy = "id";

    private String sortDir = "asc";

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
