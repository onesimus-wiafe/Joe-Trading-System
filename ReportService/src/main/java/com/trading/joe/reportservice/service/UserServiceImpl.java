package com.trading.joe.reportservice.service;

import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.Status;
import com.trading.joe.reportservice.dtos.UserDto;
import com.trading.joe.reportservice.exceptions.ResourceNotFoundException;
import com.trading.joe.reportservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private NatsService natsService;

    @PostConstruct
    public void userSubscriptionEvent() throws ResourceNotFoundException {
        System.out.println("Testing nats subscribe");
        try {
            System.out.println("hereee");
//            natsService.subscribe(Event.USER_CREATED, UserEventDto.class, this::saveCreateEvent);
//            natsService.subscribe(Event.USER_UPDATED, UserEventDto.class, this::saveUpdateEvent);
//            natsService.subscribe(Event.USER_DELETED, UserEventDto.class, this::saveDeleteEvent);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }
    }



}
