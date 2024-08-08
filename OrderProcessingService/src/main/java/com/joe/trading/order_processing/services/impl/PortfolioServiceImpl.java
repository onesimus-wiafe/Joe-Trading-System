package com.joe.trading.order_processing.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.Stock;
import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.CreatePortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioFilterRequestDto;
import com.joe.trading.order_processing.entities.enums.PortfolioState;
import com.joe.trading.order_processing.mappers.PortfolioMapper;
import com.joe.trading.order_processing.repositories.jpa.PortfolioRepository;
import com.joe.trading.order_processing.repositories.jpa.UserRepository;
import com.joe.trading.order_processing.services.PortfolioService;
import com.joe.trading.order_processing.specifications.PortfolioSpecification;
import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.exceptions.InternalServerError;
import com.joe.trading.shared.exceptions.ResourceNotFoundException;
import com.joe.trading.shared.nats.NatsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final UserRepository userRepo;
    private final PortfolioRepository portRepo;
    private final NatsService natsService;
    private final PortfolioMapper portfolioMapper;

    @Override
    public Portfolio createPortfolio(Long userId, CreatePortfolioRequestDTO portfolio) throws AccessDeniedException {
        User user;
        try {
            user = userRepo.findById(userId).get();
        } catch (NoSuchElementException e) {
            throw new AccessDeniedException("Authenticated user does not exist");
        }

        Portfolio newPortfolio = new Portfolio();
        newPortfolio.setName(portfolio.getName());
        newPortfolio.setDescription(portfolio.getDescription());

        newPortfolio.setUser(user);

        var response = portRepo.save(newPortfolio);

        try {
            natsService.publish(Event.PORTFOLIO_CREATED, portfolioMapper.toPortfolioEventDto(response));
        } catch (JsonProcessingException e) {
            throw new InternalServerError("Error while creating portfolio");
        }

        return response;
    }

    @Override
    public Boolean deletePortfolio(Long userId, Long portfolioId) throws AccessDeniedException {
        User user;
        try {
            user = userRepo.findById(userId).get();
        } catch (NoSuchElementException e) {
            throw new AccessDeniedException("Authenticated user does not exist");
        }

        List<Portfolio> userPortfolio = user.getPortfolios();

        Portfolio portfolioToDelete = portRepo.findById(portfolioId).get();

        userPortfolio.remove(portfolioToDelete);

        Portfolio userDefaultPortfolio = this.getDefaultPortfolio(userId);

        userPortfolio.remove(userDefaultPortfolio);

        Set<Stock> defaultStocks = userDefaultPortfolio.getStocks();

        portfolioToDelete.getStocks().forEach(stock -> {
            stock.setPortfolio(userDefaultPortfolio);

            Stock similarStock = defaultStocks.stream().filter(stock1 -> stock1.getTicker().equals(stock.getTicker()))
                    .findFirst().orElse(null);

            if (similarStock == null) {
                defaultStocks.add(stock);
            } else {
                defaultStocks.remove(similarStock);

                similarStock.setQuantity(similarStock.getQuantity() + stock.getQuantity());
                similarStock.setStockValue(similarStock.getStockValue() + stock.getStockValue());
                List<Order> orders = similarStock.getOrders();
                orders.addAll(stock.getOrders());
                similarStock.setOrders(orders);

                defaultStocks.add(similarStock);
            }
        });

        userDefaultPortfolio.setStocks(defaultStocks);

        userPortfolio.add(userDefaultPortfolio);

        portRepo.saveAll(userPortfolio);

        portRepo.delete(portfolioToDelete);

        return portRepo.findById(userId).isEmpty();
    }

    @Override
    public Portfolio getDefaultPortfolio(Long id) throws AccessDeniedException {
        User user;
        try {
            user = userRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new AccessDeniedException("Authenticated user does not exist");
        }

        List<Portfolio> userPortfolios = user.getPortfolios();

        return userPortfolios.stream().filter(portfolio -> portfolio.getState().equals(PortfolioState.DEFAULT)).toList()
                .get(0);
    }

    @Override
    public Page<Portfolio> getPortfoliosByUserId(Long userId, PortfolioFilterRequestDto filter) throws AccessDeniedException {
        User user;
        try {
            user = userRepo.findById(userId).get();
        } catch (NoSuchElementException e) {
            throw new AccessDeniedException("Authenticated user does not exist");
        }

        Specification<Portfolio> spec = Specification.where(null);

        if (user.getAccountType().equals(AccountType.USER)) {
            spec = spec.and(PortfolioSpecification.hasUserId(userId));
        }

        if (filter.getName() != null) {
            spec = spec.and(PortfolioSpecification.hasName(filter.getName()));
        }
        if (filter.getDescription() != null) {
            spec = spec.and(PortfolioSpecification.hasDescription(filter.getDescription()));
        }
        if (filter.getCreatedFrom() != null && filter.getCreatedTo() != null) {
            spec = spec.and(PortfolioSpecification.createdAtBetween(filter.getCreatedFrom(),
                    filter.getCreatedTo()));
        }

        Pageable pageable = PageRequest.of(
                filter.getPage() - 1,
                filter.getSize(),
                filter.getSortDir().equalsIgnoreCase("desc")
                        ? Sort.by(filter.getSortBy()).descending()
                        : Sort.by(filter.getSortBy()).ascending());

        return portRepo.findAll(spec, pageable);
    }

    @Override
    public Portfolio getPortfolio(Long userId, Long portfolioId) throws ResourceNotFoundException, AccessDeniedException {
        User principal;
        try {
            principal = userRepo.findById(userId).get();
        } catch (NoSuchElementException e) {
            throw new AccessDeniedException("Authenticated user does not exist");
        }

        Portfolio portfolio;
        try {
            portfolio = portRepo.findById(portfolioId).get();
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Portfolio does not exist");
        }

        var user = portfolio.getUser();

        if (user.getId().equals(userId) || principal.getAccountType().equals(AccountType.ADMIN)) {
            return portfolio;
        }

        throw new AccessDeniedException("User does not own this portfolio");
    }

    @Override
    public void createDefaultPortolio(Long userId) {
        userRepo.findById(userId).ifPresent(user -> {
            Portfolio newPortfolio = new Portfolio("Default", PortfolioState.DEFAULT);
            newPortfolio.setName("Default");
            newPortfolio.setDescription("This is the default portfolio");

            newPortfolio.setUser(user);

            var response = portRepo.save(newPortfolio);

            try {
                natsService.publish(Event.PORTFOLIO_CREATED, portfolioMapper.toPortfolioEventDto(response));
            } catch (JsonProcessingException e) {
                throw new InternalServerError("Error while creating portfolio");
            }
        });
    }
}
