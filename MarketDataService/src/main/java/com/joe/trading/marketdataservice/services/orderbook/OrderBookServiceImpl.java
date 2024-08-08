package com.joe.trading.marketdataservice.services.orderbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.services.enums.Ticker;
import com.joe.trading.shared.dtos.OrderBook;
import com.joe.trading.shared.dtos.OrderBookUpdateDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderBookServiceImpl implements OrderBookService{

    private final NatsService natsService;
    private final RestTemplate restTemplate;

    private final String exchange1Url = "https://exchange.matraining.com";
    private final String exchange2Url = "https://exchange2.matraining.com";
    private final Map<String, List<OrderBook>> fullOrderBook = new HashMap<>();

    @Autowired
    public OrderBookServiceImpl(NatsService natsService, RestTemplate restTemplate) {
        this.natsService = natsService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void publishOrderBook() throws JsonProcessingException {
        Arrays.stream(Ticker.values()).toList().forEach(
                ticker -> {
                    String product = ticker.toString();

                    try {
                        this.publishOrderBook(product);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public void publishOrderBook(String product) throws JsonProcessingException{

        this.publishOrderBook(product, exchange1Url);
        this.publishOrderBook(product, exchange2Url);
    }

    @Override
    public void publishOrderBook(String product, String exchange) throws JsonProcessingException {
        getOpenAndClosedOrdersFromOneExchange(product, exchange);
    }

    private void publishEvent(String product, OrderBookUpdateDto book){
        switch (Ticker.valueOf(product.toUpperCase())){
            case IBM -> this.publishOne(Event.IBM_ORDER_BOOK, book);
            case AAPL -> this.publishOne(Event.AAPL_ORDER_BOOK, book);
            case AMZN -> this.publishOne(Event.AMZN_ORDER_BOOK, book);
            case MSFT -> this.publishOne(Event.MSFT_ORDER_BOOK, book);
            case NFLX -> this.publishOne(Event.NFLX_ORDER_BOOK, book);
            case ORCL -> this.publishOne(Event.ORCL_ORDER_BOOK, book);
            case TSLA -> this.publishOne(Event.TSLA_ORDER_BOOK, book);
            case GOOGL -> this.publishOne(Event.GOOGL_ORDER_BOOK, book);
        }
    }


    private void publishOne(Event event, OrderBookUpdateDto book){
        try {
            natsService.publish(event, book);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private OrderBookUpdateDto getFromExchange(String baseUrl, String ticker, String state, String key){
        String url = baseUrl+"/orderbook/"+ticker+"/"+state;
        ResponseEntity<OrderBook[]> response = restTemplate.getForEntity(url, OrderBook[].class);

        OrderBookUpdateDto book = new OrderBookUpdateDto();
        book.setOrderBooks(Arrays.stream(response.getBody()).toList());
        book.setSourceExchange(key);

        return book;
    }

    private void getOpenAndClosedOrdersFromOneExchange(String ticker, String exchange){
        String openKey;
        String closedKey;

        if (exchange1Url.contains(exchange.toLowerCase())){
            openKey = ticker+"_EX1_OPEN";
            closedKey = ticker+"_EX1_CLOSED";

            OrderBookUpdateDto book1 = getFromExchange(exchange1Url, ticker, "open", openKey);
            OrderBookUpdateDto book2 = getFromExchange(exchange1Url, ticker, "closed", closedKey);

            this.publishEvent(ticker, book1);
            this.publishEvent(ticker, book2);

        }
        else{
            openKey = ticker+"_EX2_OPEN";
            closedKey = ticker+"_EX2_CLOSED";

            OrderBookUpdateDto book1 = getFromExchange(exchange2Url, ticker, "open", openKey);
            OrderBookUpdateDto book2 = getFromExchange(exchange2Url, ticker, "closed", closedKey);

            this.publishEvent(ticker, book1);
            this.publishEvent(ticker, book2);
        };
    }
}
