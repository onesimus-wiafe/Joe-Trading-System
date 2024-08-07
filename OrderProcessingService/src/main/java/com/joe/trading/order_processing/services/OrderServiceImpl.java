package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.*;
import com.joe.trading.order_processing.entities.cache.InternalOpenOrder;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.dto.OrderResponseDTO;
import com.joe.trading.order_processing.entities.enums.*;
import com.joe.trading.order_processing.repositories.jpa.ExchangeRepository;
import com.joe.trading.order_processing.repositories.jpa.OrderRepository;
import com.joe.trading.order_processing.repositories.jpa.TradeRepository;
import com.joe.trading.order_processing.repositories.jpa.UserRepository;
import com.joe.trading.order_processing.repositories.redis.dao.InternalOpenOrderDAO;
import com.joe.trading.order_processing.repositories.redis.dao.OrderBookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final RestTemplate restTemplate;

    private final OrderRepository orderRepo;

    private final ExchangeRepository exchangeRepo;

    private final TradeRepository tradeRepo;
    private final OrderBookDAO orderBookRepo;
    private final InternalOpenOrderDAO internalOpenOrderRepo;
    private final UserRepository userRepo;

    @Value("${exchange.private.api.key}")
    String privateApiKey;

    @Autowired
    public OrderServiceImpl(RestTemplate restTemplate, OrderRepository orderRepo, ExchangeRepository exchangeRepo, TradeRepository tradeRepo, OrderBookDAO orderBookRepo, OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor, InternalOpenOrderDAO internalOpenOrderRepo, UserRepository userRepo) {
        this.restTemplate = restTemplate;
        this.orderRepo = orderRepo;
        this.exchangeRepo = exchangeRepo;
        this.tradeRepo = tradeRepo;
        this.orderBookRepo = orderBookRepo;
        this.internalOpenOrderRepo = internalOpenOrderRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream().map(Order::toOrderResponseDTO).toList();
    }

    @Override
    public OrderResponseDTO saveOrder(Order order){

        List<Exchange> exchanges = exchangeRepo.findAll();

        Side tradeSide = order.getSide().equals(Side.BUY) ? Side.SELL : Side.BUY;

        List<OrderBook> openOrders = filterOrderBook(tradeSide, order.getOrderType(), getOrderBookFromCache(String.valueOf(order.getTicker()), order.getExchanges()));

        Order completedOrder = makeOrder(order, openOrders, exchanges);

        return orderRepo.save(completedOrder).toOrderResponseDTO();
    }

    @Override
    public OrderResponseDTO cancelOrder(Long id){
        OrderResponseDTO response = new OrderResponseDTO();

        Order order = orderRepo.findById(id).orElseThrow();

        return switch (order.getTradeStatus()){
            case OPEN -> {
                List<Trade> trades = order.getTrades();

                while (!trades.isEmpty()){
                    for (Trade trade: trades){
                        Boolean isCancelled = cancelTrade(trade.getOrderBook().getId(), trade.getExchange().getUrl());

                        if (isCancelled){
                            trades.remove(trade);
                        }
                    }
                }

                order.setTradeStatus(TradeStatus.CANCELLED);
                response.setMessage("Order cancelled!");
                yield response;
            }
            case PARTIALLY_FILLED -> {
                response.setMessage("Cannot Delete Partially Filled Order.");
                yield response;
            }
            case CLOSED -> {
                response.setMessage("Order is closed");
                yield response;
            }
            case CANCELLED -> {
                response.setMessage("order is already cancelled");
                yield response;
            }
        };
    }

    @Override
    public Page<OrderResponseDTO> getAllOrdersPerUserId(OrderRequestDTO request){
        Long id = request.getUserId();
        User user = userRepo.findById(id).orElseThrow();

        List<Portfolio> portfolios = user.getPortfolios();

        List<Order> orders = new ArrayList<>();

        portfolios.forEach(portfolio -> {
            portfolio.getStocks().forEach(
                    stock -> {
                        orders.addAll(stock.getOrders());
                    }
            );
        });

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                request.getSortDir().equalsIgnoreCase("desc")
                        ? Sort.by(request.getSortBy()).descending()
                        : Sort.by(request.getSortBy()).ascending());

        List<OrderResponseDTO> responseList = orders.stream().map(Order::toOrderResponseDTO)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), responseList.size());

        List<OrderResponseDTO> paginatedList = responseList.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, responseList.size());
    }



    private Order makeOrder(Order order, List<OrderBook> openOrders, List<Exchange> exchanges) {
        String product = String.valueOf(order.getTicker());
        String side = String.valueOf(order.getSide());
        String orderType = String.valueOf(order.getOrderType());
        Double price = order.getUnitPrice();
        Integer quantity = order.getQuantity();
        String exchange = exchanges.get(0).toString();

        while (order.getQuantity() > 0){


            if (!openOrders.isEmpty()){
                OrderBook openOrder = openOrders.get(0);
                quantity = order.getQuantity() > openOrder.getQuantity() ? openOrder.getQuantity() : order.getQuantity();

                switch (order.getOrderType()){
                    case MARKET -> {
                        price = openOrder.getPrice();
                    }
                    case LIMIT -> {
                        price = switch (order.getSide()) {
                            case SELL -> {
                                if (order.getUnitPrice() > openOrder.getPrice()) {
                                    openOrders.remove(openOrder);
                                    yield order.getUnitPrice();
                                }
                                yield openOrder.getPrice();
                            }
                            case BUY -> {
                                if (order.getUnitPrice() < openOrder.getPrice()){
                                    openOrders.remove(openOrder);
                                    yield order.getUnitPrice();
                                }
                                yield openOrder.getPrice();
                            }
                        };

                        if (!openOrders.contains(openOrder)){
                            continue;
                        }

                    }
                };
                exchange = openOrder.getExchange();
            }

            final String exchangeURL = exchange;

            Exchange ex = exchanges.stream().filter(e -> e.getUrl().equals(exchangeURL)).findAny().get();

            Trade trade = buildTrade(quantity, price, product, side, orderType, ex);

            Trade madeTrade = makeTrade(trade, exchange);

            List<Trade> trades = order.getTrades();
            trades.add(madeTrade);

            order.setTrades(trades);

            int newQuantity = order.getQuantity() - quantity;
            order.setQuantity(newQuantity);

            openOrders.remove(0);

        }

        return order;
    }

    private List<OrderBook> getOrderBookFromCache(String ticker, AvailableExchanges exchanges){
        return switch (exchanges){
            case EXCHANGE1 -> this.orderBookRepo.getOrderBook(ticker+"_EX1_OPEN");
            case EXCHANGE2 -> this.orderBookRepo.getOrderBook(ticker+"_EX2_OPEN");
            case NONE -> List.of();
            case ALL -> {
                List<OrderBook> book = getOrderBookFromCache(ticker, AvailableExchanges.EXCHANGE1);
                assert book != null;
                book.addAll(Objects.requireNonNull(getOrderBookFromCache(ticker, AvailableExchanges.EXCHANGE2)));

                yield book;
            }
        };
    }

    private List<OrderBook> filterOrderBook(Side side, OrderType orderType, List<OrderBook> orderBook){
        List<OrderBook> filteredBook = new ArrayList<>(orderBook.stream().filter(
                book -> book.getSide().equals(String.valueOf(side))
        ).toList());

        List<OrderBook> filteredBySide = switch (side){
            case SELL -> {
                filteredBook.sort(Comparator.comparingDouble(OrderBook::getPrice));

                yield filteredBook;
            }
            case BUY -> {
                filteredBook.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));

                yield filteredBook;
            }
        };

        return switch (orderType){
            case LIMIT -> filteredBySide;
            case MARKET -> filteredBySide.stream().filter(book -> !book.getOrderType().equals(String.valueOf(orderType)))
                    .toList();
        };

    }

    private Trade buildTrade(Integer quantity, Double price, String ticker, String side, String tradeType, Exchange exchange){


        return new Trade(quantity, price, ticker, side, tradeType, exchange);
    }

    private Trade makeTrade(Trade trade, String exchange){

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

        String orderId = response.getBody();

        InternalOpenOrder openOrder = new InternalOpenOrder(orderId, trade.getQuantity(), 0);
        String key = trade.getTicker()+"_OPEN";
        List<InternalOpenOrder> openOrders = internalOpenOrderRepo.getInternalOrder(key);

        if (openOrders.isEmpty() || openOrders == null){
            openOrders = new ArrayList<>();
        }

        openOrders.add(openOrder);

        internalOpenOrderRepo.saveInternalOrder(key, openOrders);


        trade.setOrderBook(
                new OrderBook(trade.getTicker(), orderId, trade.getQuantity(),
                        trade.getSide(), trade.getTradeType())
        );

        return tradeRepo.save(trade);
    }

    private Boolean cancelTrade(String orderId, String exchange){
        String url = exchange + privateApiKey + "/order/"+orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Trade> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                String.class
        );

        return Boolean.valueOf(response.getBody());
    }
}
