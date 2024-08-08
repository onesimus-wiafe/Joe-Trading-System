package com.trading.joe.reportservice;

import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.dtos.UserDto;
import com.trading.joe.reportservice.entities.MarketData;
import com.trading.joe.reportservice.entities.OrderBooks;
import com.trading.joe.reportservice.exceptions.ResourceNotFoundException;
import com.trading.joe.reportservice.repository.MarketDataRepository;
import com.trading.joe.reportservice.repository.OrderBookRepository;
import jakarta.annotation.PostConstruct;

import java.util.Map;

public class NatsSubscribe {
    private NatsService natsService;
    private OrderBookRepository orderBookRepository;
    private MarketDataRepository marketDataRepository;


    @PostConstruct
    public void subscribeToRequiredEvents() throws ResourceNotFoundException {
        System.out.println("Subscribing to required Events-(User,Market Data,");
        try {
            // Users/Clients related Events
            natsService.subscribe(Event.USER_CREATED, UserEventDto.class, this::saveCreateEvent);
            natsService.subscribe(Event.USER_UPDATED, UserEventDto.class, this::saveUpdateEvent);
            natsService.subscribe(Event.USER_DELETED, UserEventDto.class, this::saveDeleteEvent);

            // Portfoliio event
            //natsService.subscribe(Ev);

            // Market data Events
            natsService.subscribe(Event.MARKET_DATA_UPDATE, Map.class, this::saveMarketDataUpdate);

            //OrderBook events
            natsService.subscribe(Event.IBM_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
            natsService.subscribe(Event.AAPL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
            natsService.subscribe(Event.AMZN_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
            natsService.subscribe(Event.MSFT_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
            natsService.subscribe(Event.NFLX_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
            natsService.subscribe(Event.ORCL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
            natsService.subscribe(Event.TSLA_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
            natsService.subscribe(Event.GOOGL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not fetch from Nats Service");
        }

    }

    private void saveOrderBookUpdate(Map map){
        var order_book = map.values();
        if (!order_book.isEmpty()) {
            orderBookRepository.save((OrderBooks) order_book);
        }
    }

    private void saveMarketDataUpdate(Map map){
        var market_data = map.values();
        if (!market_data.isEmpty()) {
            marketDataRepository.save((MarketData) market_data);
        }
    }

    public void saveCreateEvent(UserEventDto userEventDto) {
        UserDto user = new UserDto();
        userRepository.save(user.userCreated(userEventDto));
    }

    public void saveUpdateEvent(UserEventDto userEventDto) {
        var userOptional = userRepository.findById(userEventDto.getId());
        userOptional.ifPresentOrElse(user -> {
            user.setName(userEventDto.getName());
            user.setEmail(userEventDto.getEmail());
            user.setAccountType(AccountType.valueOf(userEventDto.getAccountType()));
            user.setCreatedAt(userEventDto.getCreatedAt());
            user.setUpdatedAt(userEventDto.getUpdatedAt());
            user.setAction(Status.UPDATED);

            userRepository.save(user);
        }, () -> System.err.println("DATA INCONSISTENCY"));
    }

    public void saveDeleteEvent(UserEventDto userEventDto) {
        var userOptional = userRepository.findById(userEventDto.getId());
        userOptional.ifPresentOrElse(user -> {
            userRepository.deleteById(userEventDto.getId());

        }, () -> System.err.println("DATA INCONSISTENCY"));
    }

}
