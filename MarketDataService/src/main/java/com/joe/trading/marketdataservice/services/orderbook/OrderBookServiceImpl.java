package com.joe.trading.marketdataservice.services.orderbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.model.OrderBook;
import com.joe.trading.marketdataservice.services.enums.Ticker;
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
        fullOrderBook.putAll(getOpenOrders(product));
        fullOrderBook.putAll(getClosedOrders(product));

        publishEvent(product);
    }

    @Override
    public void publishOrderBook(String product, String exchange){
        fullOrderBook.putAll(getOpenAndClosedOrdersFromOneExchange(product, exchange));

        publishEvent(product);
    }

    private void publishEvent(String product){
        switch (Ticker.valueOf(product.toUpperCase())){
            case IBM -> this.publishOne(Event.IBM_ORDER_BOOK);
            case AAPL -> this.publishOne(Event.AAPL_ORDER_BOOK);
            case AMZN -> this.publishOne(Event.AMZN_ORDER_BOOK);
            case MSFT -> this.publishOne(Event.MSFT_ORDER_BOOK);
            case NFLX -> this.publishOne(Event.NFLX_ORDER_BOOK);
            case ORCL -> this.publishOne(Event.ORCL_ORDER_BOOK);
            case TSLA -> this.publishOne(Event.TSLA_ORDER_BOOK);
            case GOOGL -> this.publishOne(Event.GOOGL_ORDER_BOOK);
        }
    }


    private void publishOne(Event event){
        try {
            natsService.publish(event, fullOrderBook);
            fullOrderBook.clear();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<OrderBook> getFromExchange(String baseUrl, String ticker, String state){
        String url = baseUrl+"/orderbook/"+ticker+"/"+state;
        ResponseEntity<OrderBook[]> response = restTemplate.getForEntity(url, OrderBook[].class);

        return Arrays.asList(response.getBody());
    }

    private Map<String, List<OrderBook>> getOpenOrders(String ticker){
        String ex1Key = ticker+"_EX1_OPEN";
        String ex2Key = ticker+"_EX2_OPEN";

        Map<String, List<OrderBook>> result = new HashMap<>();

        result.put(ex1Key, getFromExchange(exchange1Url, ticker, "open"));
        result.put(ex2Key, getFromExchange(exchange2Url, ticker, "open"));

        return result;
    }

    private Map<String, List<OrderBook>> getClosedOrders(String ticker){
        String ex1Key = ticker+"_EX1_CLOSED";
        String ex2Key = ticker+"_EX2_CLOSED";

        Map<String, List<OrderBook>> result = new HashMap<>();

        result.put(ex1Key, getFromExchange(exchange1Url, ticker, "closed"));
        result.put(ex2Key, getFromExchange(exchange2Url, ticker, "closed"));

        return result;
    }

    private Map<String, List<OrderBook>> getOpenAndClosedOrdersFromOneExchange(String ticker, String exchange){
        String openKey;
        String closedKey;

        Map<String, List<OrderBook>> result = new HashMap<>();

        if (exchange1Url.contains(exchange.toLowerCase())){
            openKey = ticker+"_EX1_OPEN";
            closedKey = ticker+"_EX1_CLOSED";

            result.put(openKey, getFromExchange(exchange1Url, ticker, "open"));
            result.put(closedKey, getFromExchange(exchange1Url, ticker, "closed"));
        }
        else{
            openKey = ticker+"_EX2_OPEN";
            closedKey = ticker+"_EX2_CLOSED";

            result.put(openKey, getFromExchange(exchange2Url, ticker, "open"));
            result.put(closedKey, getFromExchange(exchange2Url, ticker, "closed"));
        }

        return result;
    }
}
