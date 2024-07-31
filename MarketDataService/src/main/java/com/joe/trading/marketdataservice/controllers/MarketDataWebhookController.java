package com.joe.trading.marketdataservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.model.OpenOrderDTO;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/md/webhook")
public class MarketDataWebhookController {

    private NatsService natsService;

    @Autowired
    public MarketDataWebhookController(NatsService natsService){
        this.natsService = natsService;
    }

    @PostMapping
    public OpenOrderDTO receiveUpdate(@RequestBody OpenOrderDTO payload) throws JsonProcessingException {

        System.out.println("Received Subscription Update: " + payload);
        if (payload.getExchange().equals("MAL1")){
            this.natsService.publish(Event.NEW_ORDER_EX1, payload);
        }
        else {
            this.natsService.publish(Event.NEW_ORDER_EX2, payload);
        }
        return payload;
    }

    @GetMapping
    public String getUpdate(){
        return "Here! Here!";
    }
    

}
