package com.joe.trading.order_processing.services.impl;

import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.entities.Trade;
import com.joe.trading.order_processing.entities.enums.TradeStatus;
import com.joe.trading.order_processing.repositories.jpa.OrderBookRepository;
import com.joe.trading.order_processing.repositories.jpa.OrderRepository;
import com.joe.trading.order_processing.repositories.jpa.TradeRepository;
import com.joe.trading.order_processing.services.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBookServiceImpl implements OrderBookService {
    private final OrderBookRepository orderBookRepository;
    private final TradeRepository tradeRepo;
    private final OrderRepository orderRepo;

    @Autowired
    public OrderBookServiceImpl(OrderBookRepository orderBookRepository, TradeRepository tradeRepo, OrderRepository orderRepo) {
        this.orderBookRepository = orderBookRepository;
        this.tradeRepo = tradeRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public void updateOrderBooks(List<OrderBook> orderBooks) {

        List<String> orderIdList = orderBooks.stream().map(OrderBook::getId).toList();

        List<OrderBook> booksToUpdate = orderBookRepository.findAllById(orderIdList);

        booksToUpdate.forEach(book -> {
            OrderBook newBook = orderBooks.stream().filter(orderBook -> orderBook.getId().equals(book.getId()))
                    .findFirst().get();

            book.setQuantity(newBook.getQuantity());
            book.setCumulatitiveQuantity(newBook.getCumulatitiveQuantity());
            book.setExecutions(newBook.getExecutions());

            if (book.getQuantity().equals(book.getCumulatitiveQuantity())){
                Trade trade = book.getTrade();
                trade.setStatus(TradeStatus.CLOSED);
                Order order = trade.getOrder();
                order.setTradeStatus(TradeStatus.PARTIALLY_FILLED);

                orderRepo.save(order);
                tradeRepo.save(trade);
            }
        });

        orderBookRepository.saveAll(orderBooks);

    }
}
