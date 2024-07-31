package com.joe.trading.marketdataservice.services;

import com.joe.trading.marketdataservice.DAO.MarketDataRepo;
import com.joe.trading.marketdataservice.model.MarketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MarketDataService {

    private final MarketDataRepo repo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public MarketDataService(MarketDataRepo repo){
        this.repo = repo;
    }

    public void saveToCache(MarketData marketData){
        this.repo.saveMarketData(marketData, marketData.getTICKER());
    }

    public void saveToCache(Map<String, MarketData> marketDataMap){
        this.repo.saveAll(marketDataMap);
    }

    public MarketData getFromCache(String ticker){
        return this.repo.getMarketData(ticker);
    }

    public Map<String, MarketData> getAllFromCache(){
        return this.repo.getAll();
    }

    public void clearCache(){
        this.repo.clearCache();
    }

    public void updateMarketData(MarketData marketData){
        this.repo.updateMarketData(marketData);
    }

    public List<MarketData> getAllMarketDataFromExchange(String exchange){
        return switch (exchange){
            case ("exchange1") -> {
                ResponseEntity<MarketData[]> response = restTemplate.getForEntity(
                  "https://exchange.matraining.com/pd", MarketData[].class
                );
                yield Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
            }
            case ("exchange2") -> {
                ResponseEntity<MarketData[]> response = restTemplate.getForEntity(
                        "https://exchange1.matraining.com/pd", MarketData[].class
                );
                yield Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
            }
            default -> throw new IllegalStateException("Unexpected value: " + exchange);
        };
    }

    public MarketData getMarketDataFromExchange(String exchange, String ticker){
        return switch (exchange){
            case ("exchange1") -> {
                ResponseEntity<MarketData> response = restTemplate.getForEntity(
                        "https://exchange.matraining.com/pd/"+ticker.toUpperCase(), MarketData.class
                );
                yield response.getBody();
            }
            case ("exchange2") -> {
                ResponseEntity<MarketData> response = restTemplate.getForEntity(
                        "https://exchange1.matraining.com/pd/"+ticker.toUpperCase(), MarketData.class
                );
                yield response.getBody();
            }
            default -> throw new IllegalStateException("Unexpected value: " + exchange);
        };
    }

}
