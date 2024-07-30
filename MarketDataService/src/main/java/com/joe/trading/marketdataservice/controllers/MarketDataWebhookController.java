package com.joe.trading.marketdataservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/md")
public class MarketDataWebhookController {

    @PostMapping("/webhook")
    public void receiveUpdate(@RequestBody String payload){

        System.out.println("Received Subscription Update: " + payload);

        return;
    }

}
