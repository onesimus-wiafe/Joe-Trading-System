package com.joe.trading.order_processing.services.postconstuct;

import com.joe.trading.order_processing.entities.Exchange;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.Ticker;
import com.joe.trading.order_processing.repositories.jpa.ExchangeRepository;
import com.joe.trading.order_processing.repositories.redis.dao.MarketDataDAO;
import com.joe.trading.order_processing.repositories.redis.dao.OrderBookDAO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MarketDataSetup {

    private final MarketDataDAO marketDataRepo;
    private final ExchangeRepository exchangeRepo;
    private final OrderBookDAO orderBookRepo;

    @Value("${exchange.exchange1.url}")
    private String exchange1URL;
    @Value("${exchange.exchange2.url}")
    private String exchange2URL;

    @Autowired
    public MarketDataSetup(MarketDataDAO marketDataRepo, ExchangeRepository exchangeRepo, OrderBookDAO orderBookRepo) {
        this.marketDataRepo = marketDataRepo;
        this.exchangeRepo = exchangeRepo;
        this.orderBookRepo = orderBookRepo;
    }

    @PostConstruct
    public void setUpMarketData(){
        marketDataRepo.clearCache();
        orderBookRepo.clearCache();
        Arrays.stream(Ticker.values()).forEach(ticker -> saveNullMarketDataToCache(String.valueOf(ticker)));
    }

    private void saveNullMarketDataToCache(String ticker){
        String keyEx1 = ticker+"_EX1";
        String keyEx2 = ticker+"_EX2";

        String orderBookKeyEx1 = ticker+"_EX1_OPEN";
        String orderBookKeyEx2 = ticker+"_EX2_OPEN";

        marketDataRepo.saveMarketData(null, keyEx1);
        marketDataRepo.saveMarketData(null, keyEx2);
        orderBookRepo.saveOrderBook(orderBookKeyEx1, null);
        orderBookRepo.saveOrderBook(orderBookKeyEx2, null);
    }

    @PostConstruct
    public void setUpExchanges(){
        List<Exchange> exchanges = exchangeRepo.findAll();
        if (exchanges.isEmpty()){
            Exchange exchange1 = new Exchange(exchange1URL, AvailableExchanges.EXCHANGE1);
            Exchange exchange2 = new Exchange(exchange2URL, AvailableExchanges.EXCHANGE2);
            exchangeRepo.saveAll(List.of(exchange1, exchange2));
        }
    }
}
