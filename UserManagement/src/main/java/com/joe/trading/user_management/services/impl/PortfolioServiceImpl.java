package com.joe.trading.user_management.services.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.shared.dtos.PortfolioEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.repository.PortfolioRepository;
import com.joe.trading.user_management.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl {
    private PortfolioRepository portfolioRepository;
    private NatsService natsService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @PostConstruct
    private void handlePortfolioDeleteMsg() {
        natsService.subscribe(Event.PORTFOLIO_DELETED, PortfolioEventDto.class, this::deletePortfolio);
    }

    private void deletePortfolio(PortfolioEventDto portfolio) {
        portfolioRepository.deleteById(portfolio.getId());

        var user = userRepository.findById(portfolio.getUserId());
        user.ifPresent(u -> {
            if (u.getPortfolios().isEmpty()) {
                userRepository.deleteById(u.getId());
                try {
                    natsService.publish(Event.USER_DELETED, userMapper.toUserEventDto(u));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
