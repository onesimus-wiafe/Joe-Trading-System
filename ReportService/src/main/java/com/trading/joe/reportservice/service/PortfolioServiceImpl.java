package com.trading.joe.reportservice.service;

import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.exceptions.ResourceNotFoundException;
import com.trading.joe.reportservice.repository.PortfolioRepository;
import jakarta.annotation.PostConstruct;

public class PortfolioServiceImpl {

    private PortfolioRepository portfolioRepository;
    private NatsService natsService;

    @PostConstruct
    public void portfolioSubscriptionEvent() throws ResourceNotFoundException {
        System.out.println("Testing nats subscribe");
//        try {
////            natsService.subscribe(Event.USER_CREATED, UserEventDto.class,this::saveCreateEvent);
////            natsService.subscribe(Event.USER_UPDATED, UserEventDto.class, this::saveUpdateEvent);
////            natsService.subscribe(Event.DELETE_PORTFOLIO_REQUEST, UserEventDto.class, this::saveDeleteEvent);
//        } catch (Exception e) {
//            throw new ResourceNotFoundException("Could not fetch from Nats Service");
//        }
    }

}
