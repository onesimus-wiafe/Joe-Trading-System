package com.joe.trading.order_processing.entities.dto;

import com.joe.trading.order_processing.entities.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponseDTO {
    private Long portfolioId;
    private String portfolioName;
    private Double portfolioValue;
    private List<Stock> stocks;
}
