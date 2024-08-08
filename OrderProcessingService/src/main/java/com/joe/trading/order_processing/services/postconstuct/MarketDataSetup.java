package com.joe.trading.order_processing.services.postconstuct;

import com.joe.trading.order_processing.entities.Exchange;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.Ticker;
import com.joe.trading.order_processing.repositories.jpa.ExchangeRepository;
import com.joe.trading.order_processing.repositories.redis.dao.MarketDataDAO;
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

    @Value("${exchange.exchange1.url}")
    private String exchange1URL;
    @Value("${exchange.exchange2.url}")
    private String exchange2URL;

    @Autowired
    public MarketDataSetup(MarketDataDAO marketDataRepo, ExchangeRepository exchangeRepo) {
        this.marketDataRepo = marketDataRepo;
        this.exchangeRepo = exchangeRepo;
    }

    @PostConstruct
    public void setUpMarketData(){
        marketDataRepo.clearCache();
        Arrays.stream(Ticker.values()).forEach(ticker -> saveNullMarketDataToCache(String.valueOf(ticker)));
    }

    private void saveNullMarketDataToCache(String ticker){
        String keyEx1 = ticker+"_EX1";
        String keyEx2 = ticker+"_EX2";

        marketDataRepo.saveMarketData(null, keyEx1);
        marketDataRepo.saveMarketData(null, keyEx2);
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
