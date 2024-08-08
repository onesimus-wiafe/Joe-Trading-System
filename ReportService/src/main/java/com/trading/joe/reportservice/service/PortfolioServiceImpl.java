package com.trading.joe.reportservice.service;

import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.entities.Portfolio;
import com.trading.joe.reportservice.exceptions.ResourceNotFoundException;
import com.trading.joe.reportservice.repository.PortfolioRepository;
import jakarta.annotation.PostConstruct;

import java.util.List;

public class PortfolioServiceImpl {

    private PortfolioRepository portfolioRepository;
    private NatsService natsService;

    @PostConstruct
    public void portfolioSubscriptionEvent() throws ResourceNotFoundException {
        System.out.println("Testing nats subscribe");
        try{
            System.out.println("Here");
            //natsService.subscribe(Event.PORTFOLIO_DELETED, PortfolioEventDto.class,);
        }
        catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }

    }


    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);

    }

    }
