package com.joe.trading.order_processing.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.CreatePortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioFilterRequestDto;
import com.joe.trading.order_processing.entities.dto.PortfolioResponseDTO;
import com.joe.trading.order_processing.mappers.PortfolioMapper;
import com.joe.trading.order_processing.services.PortfolioService;
import com.joe.trading.shared.dtos.PaginatedResponseDto;
import com.joe.trading.shared.exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioMapper portfolioMapper;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PortfolioResponseDTO> createPortfolio(@RequestBody CreatePortfolioRequestDTO request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (User) auth.getPrincipal();

        var portfolio = portfolioService.createPortfolio(principal.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(portfolioMapper.toPortfolioResponseDTO(portfolio));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> deletePortfolio(@PathVariable("id") Long portfolioId) throws ResourceNotFoundException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (User) auth.getPrincipal();

        return ResponseEntity.ok(portfolioService.deletePortfolio(principal.getId(), portfolioId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PortfolioResponseDTO> getPortfolio(@PathVariable("id") Long portfolioId) throws ResourceNotFoundException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (User) auth.getPrincipal();

        var portfolio = portfolioService.getPortfolio(principal.getId(), portfolioId);

        return ResponseEntity.ok(portfolioMapper.toPortfolioResponseDTO(portfolio));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaginatedResponseDto<PortfolioResponseDTO>> getPortfolios(PortfolioFilterRequestDto filter) throws ResourceNotFoundException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (User) auth.getPrincipal();

        var portfolios = portfolioService.getPortfoliosByUserId(principal.getId(), filter);

        var response = new PaginatedResponseDto<>(
                portfolioMapper.toPortfolioResponseDTOs(portfolios.getContent()),
                portfolios.getTotalPages(),
                portfolios.getTotalElements(),
                portfolios.getNumber() + 1
        );

        return ResponseEntity.ok(response);
    }
}
