package com.joe.trading.marketdataservice;

import com.joe.trading.marketdataservice.services.MarketDataServiceImpl;
import com.joe.trading.marketdataservice.services.orderbook.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication(scanBasePackages = "com.joe.trading")
public class MarketDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketDataServiceApplication.class, args);
    }

    @Component
    public static class StartupRunner implements CommandLineRunner {

        private final MarketDataServiceImpl mdService;
        private final OrderBookService orderBookService;

        @Autowired
        public StartupRunner(MarketDataServiceImpl mdService, OrderBookService orderBookService) {
            this.mdService = mdService;
            this.orderBookService = orderBookService;
        }

        @Override
        public void run(String... args) throws Exception {
            mdService.buildInitialCacheEntry();
            orderBookService.publishOrderBook();
        }
    }

}
