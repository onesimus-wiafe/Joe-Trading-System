package com.trading.joe.reportservice.service;

import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.entities.Users;
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
            natsService.subscribe(Event.USER_CREATED, UserEventDto.class, this::saveEvent);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }
    }
    public void saveEvent(UserEventDto userEventDto){
        userRepository.save(user(userEventDto));
    }

    @Override
    public void createUser() throws ResourceNotFoundException {
        try{
            natsService.subscribe(Event.USER_CREATED, UserEventDto.class,this::saveEvent);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }
    }

    public Users user(UserEventDto userEventDto){
        Users user = new Users();
        user.setUser_id(userEventDto.getId());
        user.setName(userEventDto.getName());
        user.setEmail(userEventDto.getEmail());
        user.setAccountType(user.getAccountType());
        user.setPendingDelete(userEventDto.getPendingDelete());
        user.setCreatedAt(userEventDto.getCreatedAt());
        user.setUpdatedAt(userEventDto.getUpdatedAt());

        return user;
    }

}
