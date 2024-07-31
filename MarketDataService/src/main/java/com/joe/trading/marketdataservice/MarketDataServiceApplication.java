package com.joe.trading.marketdataservice;

import com.joe.trading.marketdataservice.model.MarketData;
import com.joe.trading.marketdataservice.services.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
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

        return args -> {
            List<MarketData> marketDataList = new ArrayList<>();
            // get market data from Exchange 1;
            marketDataList.addAll(mdService.getAllMarketDataFromExchange("exchange1"));
            // get market data from Exchange 2;
            marketDataList.addAll(mdService.getAllMarketDataFromExchange("exchange2"))

        };
    }

}
