package com.joe.trading.marketdataservice.services.orderbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.model.OrderBook;
import com.joe.trading.marketdataservice.services.enums.Ticker;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderBookServiceImpl implements OrderBookService{

    private final NatsService natsService;
    private final RestTemplate restTemplate;

    private final String exchange1Url = "https://exchange.matraining.com";
    private final String exchange2Url = "https://exchange2.matraining.com";

    @Autowired
    public OrderBookServiceImpl(NatsService natsService, RestTemplate restTemplate) {
        this.natsService = natsService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void publishOrderBook() throws JsonProcessingException {

        Map<String, List<OrderBook>> fullOrderBook = new HashMap<>();

        Arrays.stream(Ticker.values()).toList().forEach(
                ticker -> {
                    String product = ticker.toString();

                    fullOrderBook.putAll(getOpenOrders(product));
                    fullOrderBook.putAll(getClosedOrders(product));
                    fullOrderBook.putAll(getCancelledOrders(product));
                }
        );

        System.out.println(fullOrderBook);
        natsService.publish(Event.FULL_ORDER_BOOK, fullOrderBook);
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

    private Map<String, List<OrderBook>> getCancelledOrders(String ticker){
        String ex1Key = ticker+"_EX1_CANCELLED";
        String ex2Key = ticker+"_EX2_CANCELLED";

        Map<String, List<OrderBook>> result = new HashMap<>();

        result.put(ex1Key, getFromExchange(exchange1Url, ticker, "cancelled"));
        result.put(ex2Key, getFromExchange(exchange2Url, ticker, "cancelled"));

        return result;
    }


    private List<OrderBook> getBuyOrders(List<OrderBook> orders){
        return orders.stream().filter(order -> order.getSide().equals("BUY")).toList();
    }

    private List<OrderBook> getSellOrders(List<OrderBook> orders){
        return orders.stream().filter(order -> order.getSide().equals("SELL")).toList();
    }
}

/**
 *
 */
