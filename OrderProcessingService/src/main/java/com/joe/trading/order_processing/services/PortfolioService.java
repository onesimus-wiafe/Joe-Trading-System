package com.joe.trading.order_processing.services;

import org.springframework.data.domain.Page;

import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.dto.CreatePortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioFilterRequestDto;

public interface PortfolioService {
    Portfolio createPortfolio(Long userId, CreatePortfolioRequestDTO portfolio);

    public Boolean deletePortfolio(Long userId, Long portfolioId);

    Portfolio getDefaultPortfolio(Long id);

    Portfolio getPortfolio(Long userId, Long portfolioId);

    Page<Portfolio> getPortfoliosByUserId(Long userId, PortfolioFilterRequestDto filter);
}
