package com.joe.trading.order_processing.entities.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.joe.trading.order_processing.entities.Stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double value;
    private LocalDateTime createdAt;
    private List<Stock> stocks;
}
