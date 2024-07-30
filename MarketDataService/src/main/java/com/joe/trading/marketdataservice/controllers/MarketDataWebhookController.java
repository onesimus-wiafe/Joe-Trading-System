package com.joe.trading.marketdataservice.controllers;

import com.joe.trading.marketdataservice.model.OpenOrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/md/webhook")
public class MarketDataWebhookController {

    @PostMapping
    public OpenOrderDTO receiveUpdate(@RequestBody OpenOrderDTO payload){

        System.out.println("Received Subscription Update: " + payload);
        return payload;
    }

    @GetMapping
    public String getUpdate(){
        return "Here! Here!";
    }
    

}
