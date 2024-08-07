package com.joe.trading.order_processing.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.Stock;
import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.PortfolioRequestDTO;
import com.joe.trading.order_processing.entities.dto.PortfolioResponseDTO;
import com.joe.trading.order_processing.entities.enums.PortfolioState;
import com.joe.trading.order_processing.repositories.jpa.PortfolioRepository;
import com.joe.trading.order_processing.repositories.jpa.UserRepository;
import com.joe.trading.order_processing.services.PortfolioService;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final UserRepository userRepo;
    private final PortfolioRepository portRepo;
    private final NatsService natsService;

    @Autowired
    public PortfolioServiceImpl(UserRepository userRepo, PortfolioRepository portRepo, NatsService natsService) {
        this.userRepo = userRepo;
        this.portRepo = portRepo;
        this.natsService = natsService;
    }

    @Override
    public PortfolioResponseDTO saveNewPortfolio(Long userId, String name) throws JsonProcessingException {
        User user = userRepo.findById(userId).get();

        Portfolio newPortfolio = new Portfolio(name);

        newPortfolio.setUser(user);



        PortfolioResponseDTO response = portRepo.save(newPortfolio).toPortfolioResponseDTO();
        natsService.publish(Event.PORTFOLIO_CREATED, response);

        return response;
    }

    @Override
    public Boolean deletePortfolio(PortfolioRequestDTO request) {
        User user = userRepo.findById(request.getUserId()).get();

        List<Portfolio> userPortfolio = user.getPortfolios();

        Portfolio portfolioToDelete = portRepo.findById(request.getPortfolioId()).get();

        userPortfolio.remove(portfolioToDelete);

        Portfolio userDefaultPortfolio = this.getDefaultPortfolio(request.getUserId());

        userPortfolio.remove(userDefaultPortfolio);

        List<Stock> defaultStocks = userDefaultPortfolio.getStocks();

        portfolioToDelete.getStocks().forEach(stock -> {
            stock.setPortfolio(userDefaultPortfolio);

            Stock similarStock = defaultStocks.stream().filter(stock1 -> stock1.getTicker().equals(stock.getTicker())).findFirst().orElse(null);

            if (similarStock == null){
                defaultStocks.add(stock);
            }
            else {
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

        return portRepo.findById(request.getUserId()).isEmpty();
    }

    @Override
    public Portfolio getDefaultPortfolio(Long id) {
        User user = userRepo.findById(id).get();

        List<Portfolio> userPortfolios = user.getPortfolios();

        return userPortfolios.stream().filter(portfolio -> portfolio.getState().equals(PortfolioState.DEFAULT)).toList().get(0);
    }

    @Override
    public List<Portfolio> getAllPortfolioByUserId(Long userId) {
        return List.of();
    }
}
