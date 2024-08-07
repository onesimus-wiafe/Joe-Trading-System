package com.joe.trading.order_processing.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRequestDTO {
    private Long userId;
    private String portfolioName;
    private Long portfolioId;
}
