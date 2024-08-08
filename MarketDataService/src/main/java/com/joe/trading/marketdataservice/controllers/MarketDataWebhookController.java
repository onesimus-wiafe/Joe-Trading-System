package com.joe.trading.marketdataservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.model.MarketData;
import com.joe.trading.marketdataservice.model.OpenOrderDTO;
import com.joe.trading.marketdataservice.services.MarketDataServiceImpl;
import com.joe.trading.marketdataservice.services.orderbook.OrderBookService;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/md/webhook")
public class MarketDataWebhookController {

    private final NatsService natsService;
    private final MarketDataServiceImpl mdService;
    private final OrderBookService orderBookService;

    @Autowired
    public MarketDataWebhookController(NatsService natsService, MarketDataServiceImpl mdService, OrderBookService orderBookService){
        this.natsService = natsService;
        this.mdService = mdService;
        this.orderBookService = orderBookService;
    }

    @PostMapping
    public ResponseEntity<OpenOrderDTO> receiveUpdate(@RequestBody OpenOrderDTO payload) throws JsonProcessingException {

        String ticker;
        String exchange;
        Map<String, MarketData> dataUpdate = new HashMap<>();

        System.out.println("Received Subscription Update: " + payload);
        if (payload.getExchange().equals("MAL1")){
            ticker = payload.getProduct().toUpperCase() + "_EX1";
            exchange = "Exchange1";
            dataUpdate.put(ticker, newMarketData("exchange1", payload.getProduct()));
        }
        else {
            ticker = payload.getProduct().toUpperCase() + "_EX1";
            exchange = "Exchange2";
            dataUpdate.put(ticker, newMarketData("exchange2", payload.getProduct()));
        }

        natsService.publish(Event.MARKET_DATA_UPDATE, dataUpdate);
        orderBookService.publishOrderBook(payload.getProduct(), exchange.toLowerCase());
        mdService.updateMarketData(dataUpdate.get(ticker));

        dataUpdate.clear();

        return ResponseEntity.ok(payload);
    }

    private MarketData newMarketData(String exchange, String ticker){
        return mdService.getMarketDataFromExchange(exchange, ticker);
    }

    @GetMapping
    public String getUpdate(){
        return "Here! Here!";
    }
    

}
