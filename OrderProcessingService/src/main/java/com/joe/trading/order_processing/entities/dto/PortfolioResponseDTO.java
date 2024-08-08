package com.joe.trading.order_processing.entities.dto;

import com.joe.trading.order_processing.entities.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double value;
    private String state;
    private LocalDateTime createdAt;
    private List<Stock> stocks;
}
