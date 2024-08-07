package com.joe.trading.order_processing.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.order_processing.entities.dto.PortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioResponseDTO;
import com.joe.trading.order_processing.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    public ResponseEntity<PortfolioResponseDTO> createPortfolio(@RequestBody PortfolioRequestDTO request) throws JsonProcessingException {

        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioService.saveNewPortfolio(request.getUserId(), request.getPortfolioName()));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePortfolio(PortfolioRequestDTO request){

        return ResponseEntity.ok(portfolioService.deletePortfolio(request));
    }
}
