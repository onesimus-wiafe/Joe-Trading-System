package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.Exchange;
import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.entities.Trade;
import com.joe.trading.order_processing.entities.dto.OrderResponseDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.repositories.ExchangeRepository;
import com.joe.trading.order_processing.repositories.OrderRepository;
import com.joe.trading.order_processing.repositories.TradeRepository;
import com.joe.trading.order_processing.repositories.UserRepository;
import com.joe.trading.order_processing.services.validation.OrderValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final OrderValidationService orderValidation;

    private final OrderRepository orderRepo;

    private final ExchangeRepository exchangeRepo;

    private final TradeRepository tradeRepo;
    private final UserRepository userRepository;

    @Value("${exchange.private.api.key}")
    String privateApiKey;

    public OrderServiceImpl(OrderValidationService orderValidationService, OrderRepository orderRepo, ExchangeRepository exchangeRepo, TradeRepository tradeRepo, UserRepository userRepository) {
        this.orderValidation = orderValidationService;
        this.orderRepo = orderRepo;
        this.exchangeRepo = exchangeRepo;
        this.tradeRepo = tradeRepo;
        this.userRepository = userRepository;
    }

    public OrderResponseDTO saveOrder(Order order){
        // Order has been validated,
        // The validation process will define the exchanges available for trades
        // We get all open trades from the valid exchanges
        // We create a list that holds the trades
        // We call the exchange and make trades

        List<Exchange> exchanges = exchangeRepo.findAll();

        System.out.println("EXCHANGES ARE PULLED FROM DB");
        exchanges.forEach(System.out::println);

        String side = order.getSide().equals(Side.BUY) ? "sell" : "buy";

        List<OrderBook> openTrades = openTradesOnExchange
                (order.getExchanges(), String.valueOf(order.getTicker()), exchanges, side, String.valueOf(order.getOrderType()));

        System.out.println("SHOWING ALL OPEN TRADES ON THE EXCHANGE");
        openTrades.forEach(System.out::println);
        // TODO: SPLIT BETWEEN BOTH EXCHANGES AND RUN ALGO.
        // TODO: run algorithm on one exchange
        // TODO: run algorithm on one exchange
        // TODO: validation failed


        return order.toOrderResponseDTO();
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream().map(Order::toOrderResponseDTO).toList();
    }

    // TODO: MOVE TO MARKET DATA SERVICE;
    private List<OrderBook> tempMethodGetOrderBookFromExchange(String url, String ticker, String side, String type){
        String exchangeUrl = url + "/orderbook/" + ticker.toUpperCase();

        ResponseEntity<OrderBook[]> response = restTemplate.getForEntity(
                exchangeUrl, OrderBook[].class);

       List<OrderBook> books = new ArrayList<>(Arrays.stream(response.getBody()).filter(
               book -> book.getSide().equals(side.toUpperCase()) && book.getOrderType().equals(type.toUpperCase())
       ).toList());

       books.sort(Comparator.comparingDouble(OrderBook::getPrice));

        return (books.size() < 30) ? books: books.subList(0, 30);
    }

    private List<OrderBook> openTradesOnExchange(AvailableExchanges exchanges, String ticker, List<Exchange> allExchanges, String side, String type) {
        return switch (exchanges) {
            case ALL -> flatten(allExchanges.stream().map(
                    exchange -> tempMethodGetOrderBookFromExchange(
                            exchange.toString(), ticker, side, type
                    )
            ).toList(), List.of());
            case EXCHANGE1, EXCHANGE2 ->
                    tempMethodGetOrderBookFromExchange(allExchanges.get(0).toString(), ticker, side, type);
            case NONE -> List.of();
        };
    }

    private List<OrderBook> flatten(List<List<OrderBook>> listToBreak, List<OrderBook>initial){
        return listToBreak.stream().reduce(initial, (acc, list) -> {
            acc.addAll(list);
            return acc;
        });
    }

    private Trade buildTrade(){
        return new Trade();
    }

    private void makeTrade(Trade trade, Exchange exchange){

        String url = exchange + privateApiKey + "/order";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Trade> request = new HttpEntity<>(trade, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        String bookId = response.getBody();

        trade.setOrderBook(
                new OrderBook(trade.getTicker(), bookId, trade.getQuantity(),
                        trade.getSide(), trade.getTradeType())
        );

        tradeRepo.save(trade);

    }
}
