package com.joe.trading.marketdataservice;

import com.joe.trading.marketdataservice.services.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.joe.trading")
public class MarketDataServiceApplication {

    private final MarketDataService mdService;
    @Autowired
    public MarketDataServiceApplication(MarketDataService mdService) {
        this.mdService = mdService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MarketDataServiceApplication.class, args);
    }

    public CommandLineRunner startupMDRunner(){

        return args -> mdService.buildInitialCacheEntry();
    }

}
