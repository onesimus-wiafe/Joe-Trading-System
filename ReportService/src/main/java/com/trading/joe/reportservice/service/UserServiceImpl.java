package com.trading.joe.reportservice.service;

import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.dtos.UserDto;
import com.trading.joe.reportservice.exceptions.ResourceNotFoundException;
import com.trading.joe.reportservice.repository.PortfolioRepository;
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
            natsService.subscribe(Event.USER_CREATED, UserEventDto.class,this::saveCreateEvent);
            natsService.subscribe(Event.USER_UPDATED, UserEventDto.class, this::saveUpdateEvent);
            natsService.subscribe(Event.USER_DELETED, UserEventDto.class, this::saveDeleteEvent);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }
    }


    public void saveCreateEvent(UserEventDto userEventDto){
        UserDto user = new UserDto();
        userRepository.save(user.userCreated(userEventDto));
    }

    public void saveUpdateEvent(UserEventDto userEventDto){
        UserDto user = new UserDto();
        userRepository.save(user.userUpdated(userEventDto));
    }

    public void saveDeleteEvent(UserEventDto userEventDto){
        UserDto user = new UserDto();
        userRepository.save(user.userDeleted(userEventDto));
    }



}
