package com.joe.trading.order_processing.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.joe.trading.order_processing.entities.Exchange;
import com.joe.trading.order_processing.entities.Order;
import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.Stock;
import com.joe.trading.order_processing.entities.Trade;
import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.dto.OrderResponseDTO;
import com.joe.trading.order_processing.entities.dto.ProductOrder;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.OrderType;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.enums.TradeStatus;
import com.joe.trading.order_processing.repositories.jpa.ExchangeRepository;
import com.joe.trading.order_processing.repositories.jpa.OrderBookRepository;
import com.joe.trading.order_processing.repositories.jpa.OrderRepository;
import com.joe.trading.order_processing.repositories.jpa.PortfolioRepository;
import com.joe.trading.order_processing.repositories.jpa.StockRepository;
import com.joe.trading.order_processing.repositories.jpa.TradeRepository;
import com.joe.trading.order_processing.repositories.jpa.UserRepository;
import com.joe.trading.order_processing.repositories.redis.dao.InternalOpenOrderDAO;
import com.joe.trading.order_processing.repositories.redis.dao.OrderBookDAO;
import com.joe.trading.order_processing.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final RestTemplate restTemplate;

    private final OrderRepository orderRepo;

    private final ExchangeRepository exchangeRepo;

    private final TradeRepository tradeRepo;
    private final OrderBookDAO orderBookRepo;
    private final InternalOpenOrderDAO internalOpenOrderRepo;
    private final UserRepository userRepo;
    private final PortfolioRepository portfolioRepo;
    public final StockRepository stockRepository;
    private final OrderBookRepository orderBookRepository;

    @Value("${exchange.private.api.key}")
    String privateApiKey;

    public OrderServiceImpl(RestTemplate restTemplate, OrderRepository orderRepo, ExchangeRepository exchangeRepo,
            TradeRepository tradeRepo, OrderBookDAO orderBookRepo,
            OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor,
            InternalOpenOrderDAO internalOpenOrderRepo, UserRepository userRepo, PortfolioRepository portfolioRepo,
            StockRepository stockRepository, OrderBookRepository orderBookRepository) {
        this.restTemplate = restTemplate;
        this.orderRepo = orderRepo;
        this.exchangeRepo = exchangeRepo;
        this.tradeRepo = tradeRepo;
        this.orderBookRepo = orderBookRepo;
        this.internalOpenOrderRepo = internalOpenOrderRepo;
        this.userRepo = userRepo;
        this.portfolioRepo = portfolioRepo;
        this.stockRepository = stockRepository;
        this.orderBookRepository = orderBookRepository;
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream().map(Order::toOrderResponseDTO).toList();
    }

    @Override
    public OrderResponseDTO saveOrder(Order order, Long portfolioId) {

        // TODO: REMOVE PRINT OUTS
        Portfolio portfolio = portfolioRepo.findById(portfolioId).orElseThrow();
        Set<Stock> stocks = portfolio.getStocks();

        List<Exchange> exchanges = exchangeRepo.findAll();

        Side tradeSide = order.getSide().equals(Side.BUY) ? Side.SELL : Side.BUY;

        List<OrderBook> openOrders = filterOrderBook(tradeSide, order.getOrderType(),
                getOrderBookFromCache(String.valueOf(order.getTicker()), order.getExchanges()));

        Order completedOrder = makeOrder(order, openOrders, exchanges);

        completedOrder.setStock(null);

        Order orderMade = orderRepo.save(completedOrder);

        orderMade.getTrades().forEach(trade -> trade.setOrder(orderMade));

        Stock stock = stocks.stream()
                .filter(s -> s.getTicker().equals(order.getTicker()))
                .findAny().orElse(new Stock(String.valueOf(order.getTicker()), 0));

        orderMade.setStock(stock);
    


        List<Order> orders = stock.getOrders();

        orders.add(orderMade);

        stock.setOrders(orders);

        var savedStock = stockRepository.save(stock);

        stocks.add(savedStock);

        portfolio.setStocks(stocks);

        portfolioRepo.save(portfolio);
    

        return orderMade.toOrderResponseDTO();
    }

    @Override
    public OrderResponseDTO cancelOrder(Long orderId, Long userId) {
        OrderResponseDTO response = new OrderResponseDTO();

        Order order = orderRepo.findById(orderId).orElseThrow();

        if (!userId.equals(order.getUserId())) {
            response.setMessage("User is not the owner of this order");
            return response;
        }

        return switch (order.getTradeStatus()) {
            case OPEN -> {
                List<Trade> trades = order.getTrades();

                while (!trades.isEmpty()) {
                    for (Trade trade : trades) {
                        Boolean isCancelled = cancelTrade(trade.getOrderBook().getId(), trade.getExchange());

                        if (isCancelled) {
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
    public Page<OrderResponseDTO> getAllOrdersPerUserId(Long userId, OrderRequestDTO request) {
        User user = userRepo.findById(userId).orElseThrow();

        List<Portfolio> portfolios = user.getPortfolios();

        List<Order> orders = new ArrayList<>();

        portfolios.forEach(portfolio -> {
            portfolio.getStocks().forEach(
                    stock -> {
                        orders.addAll(stock.getOrders());
                    });
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

        while (order.getQuantity() > 0) {

            if (!openOrders.isEmpty()) {
                OrderBook openOrder = openOrders.get(0);
                quantity = order.getQuantity() > openOrder.getQuantity() ? openOrder.getQuantity()
                        : order.getQuantity();

                switch (order.getOrderType()) {
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
                                if (order.getUnitPrice() < openOrder.getPrice()) {
                                    openOrders.remove(openOrder);
                                    yield order.getUnitPrice();
                                }
                                yield openOrder.getPrice();
                            }
                        };

                        if (!openOrders.contains(openOrder)) {
                            continue;
                        }

                    }
                }
                exchange = openOrder.getExchange();
            }

            final String exchangeURL = exchange;

            Trade trade = buildTrade(quantity, price, product, side, orderType, exchangeURL);

            Trade madeTrade = makeTrade(trade, exchange);

            madeTrade.setOrder(null);

            Trade savedTrade = tradeRepo.save(madeTrade);

            List<Trade> trades = order.getTrades();
            trades.add(savedTrade);

            order.setTrades(trades);

            int newQuantity = order.getQuantity() - quantity;
            order.setQuantity(newQuantity);

            if (!openOrders.isEmpty())
                openOrders.remove(0);
        }

        return order;
    }

    private List<OrderBook> getOrderBookFromCache(String ticker, AvailableExchanges exchanges) {
        return switch (exchanges) {
            case EXCHANGE1 -> {
                List<OrderBook> data = this.orderBookRepo.getOrderBook(ticker + "_EX1_OPEN").orElse(null);
                if (data == null) {
                    yield new ArrayList<>();
                }
                yield data;
            }
            case EXCHANGE2 -> {
                List<OrderBook> data = this.orderBookRepo.getOrderBook(ticker + "_EX2_OPEN").orElse(null);
                if (data == null) {
                    yield new ArrayList<>();
                }
                yield data;
            }
            case NONE -> new ArrayList<>();
            case ALL -> {
                List<OrderBook> book = getOrderBookFromCache(ticker, AvailableExchanges.EXCHANGE1);
                assert book != null;
                if (book.isEmpty()) {
                    yield getOrderBookFromCache(ticker, AvailableExchanges.EXCHANGE2);
                }
                book.addAll(Objects.requireNonNull(getOrderBookFromCache(ticker, AvailableExchanges.EXCHANGE2)));

                yield book;
            }
        };
    }

    private List<OrderBook> filterOrderBook(Side side, OrderType orderType, List<OrderBook> orderBook) {
        List<OrderBook> filteredBook = new ArrayList<>(orderBook.stream().filter(
                book -> book.getSide().equals(String.valueOf(side))).toList());

        List<OrderBook> filteredBySide = switch (side) {
            case SELL -> {
                filteredBook.sort(Comparator.comparingDouble(OrderBook::getPrice));

                yield filteredBook;
            }
            case BUY -> {
                filteredBook.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));

                yield filteredBook;
            }
        };

        return switch (orderType) {
            case LIMIT -> filteredBySide;
            case MARKET ->
                filteredBySide.stream().filter(book -> !book.getOrderType().equals(String.valueOf(orderType)))
                        .collect(Collectors.toList());
        };

    }

    private Trade buildTrade(Integer quantity, Double price, String ticker, String side, String tradeType,
            String exchange) {

        return new Trade(quantity, price, ticker, side, tradeType, exchange);
    }

    private Trade makeTrade(Trade trade, String exchange) {

        String url = exchange + "/" + privateApiKey + "/order";

        String ex = exchange.contains("exchange2") ? "_EX2" : "_EX1";

        ProductOrder order = new ProductOrder(trade.getTicker(), trade.getQuantity(), trade.getPrice(), trade.getSide(),
                trade.getTradeType());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductOrder> request = new HttpEntity<>(order, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class);

        String orderId = response.getBody();

        String key = trade.getTicker() + ex + "_OPEN";
        List<String> openOrders = internalOpenOrderRepo.getInternalOrder(key);

        if (openOrders == null || openOrders.isEmpty()) {
            openOrders = new ArrayList<>();
        }

        openOrders.add(orderId);

        internalOpenOrderRepo.saveInternalOrder(key, openOrders);
        
        OrderBook newOrderBook = new OrderBook(trade.getTicker(), orderId, trade.getQuantity(), trade.getSide(), trade.getTradeType());
        newOrderBook.setTrade(null);

        OrderBook savedBook = orderBookRepository.save(newOrderBook);

        trade.setOrderBook(savedBook);

        return trade;
    }

    private Boolean cancelTrade(String orderId, String exchange) {
        String url = exchange + privateApiKey + "/order/" + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Trade> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                String.class);

        return Boolean.valueOf(response.getBody());
    }
}
