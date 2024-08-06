package com.trading.joe.reportservice.service;

import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.exceptions.ResourceNotFoundException;
import com.trading.joe.reportservice.repository.PortfolioRepository;
import com.trading.joe.reportservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportingServiceImpl implements ReportingService{

    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;
    private NatsService natsService;

    @PostConstruct
    public void userCreatedEvent() throws ResourceNotFoundException {
        System.out.println("Testing nats subscribe");
        try {
            natsService.subscribe(Event.USER_CREATED, UserEventDto.class, System.out::println);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }
    }

    @Override
    public void createUser() throws ResourceNotFoundException {
        try{
            natsService.subscribe(Event.USER_CREATED, UserEventDto.class, System.out::println);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }

    }

}
