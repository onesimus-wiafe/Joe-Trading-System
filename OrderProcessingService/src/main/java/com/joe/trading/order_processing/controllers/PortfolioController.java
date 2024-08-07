package com.joe.trading.order_processing.controllers;

import com.joe.trading.order_processing.entities.dto.PortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioResponseDTO;
import com.joe.trading.order_processing.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    public ResponseEntity<PortfolioResponseDTO> createPortfolio(PortfolioRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioService.saveNewPortfolio(request.getUserId(), request.getPortfolioName()));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePortfolio(PortfolioRequestDTO request){

        return ResponseEntity.ok(portfolioService.deletePortfolio(request));
    }
}
