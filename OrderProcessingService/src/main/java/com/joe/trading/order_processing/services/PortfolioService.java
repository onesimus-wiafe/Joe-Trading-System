package com.joe.trading.order_processing.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.dto.PortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioResponseDTO;

import java.util.List;

public interface PortfolioService {
    PortfolioResponseDTO saveNewPortfolio(Long userId, String name) throws JsonProcessingException;

    Boolean deletePortfolio(PortfolioRequestDTO request);

    Portfolio getDefaultPortfolio(Long id);
    List<Portfolio> getAllPortfolioByUserId(Long userId);
}
