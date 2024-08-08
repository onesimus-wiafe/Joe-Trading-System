package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.dto.CreatePortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioFilterRequestDto;
import com.joe.trading.shared.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;

public interface PortfolioService {
    Portfolio createPortfolio(Long userId, CreatePortfolioRequestDTO portfolio);

    public Boolean deletePortfolio(Long userId, Long portfolioId) throws AccessDeniedException;

    Portfolio getDefaultPortfolio(Long id) throws AccessDeniedException;

    Portfolio getPortfolio(Long userId, Long portfolioId) throws ResourceNotFoundException, AccessDeniedException;

    Page<Portfolio> getPortfoliosByUserId(Long userId, PortfolioFilterRequestDto filter)
            throws AccessDeniedException;

    void createDefaultPortolio(Long userId);
}
