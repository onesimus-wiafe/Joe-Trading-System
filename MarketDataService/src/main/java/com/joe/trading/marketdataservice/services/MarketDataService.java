package com.joe.trading.marketdataservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.DAO.MarketDataRepo;
import com.joe.trading.marketdataservice.model.MarketData;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MarketDataService {

    private final MarketDataRepo repo;
    private final RestTemplate restTemplate;
    private final NatsService natsService;

    @Autowired
    public MarketDataService(MarketDataRepo repo, RestTemplate restTemplate, NatsService natsService){
        this.repo = repo;
        this.restTemplate = restTemplate;
        this.natsService = natsService;
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

    private List<MarketData> getAllMarketDataFromExchange(String exchange){
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

    public void buildInitialCacheEntry() throws JsonProcessingException {
        List<MarketData> marketDataList = new ArrayList<>();
        // get market data from Exchange 1;
        marketDataList.addAll(this.getAllMarketDataFromExchange("exchange1"));
        // get market data from Exchange 2;
        marketDataList.addAll(this.getAllMarketDataFromExchange("exchange2"));

        // change to cache agreeable format.
        Map<String, MarketData> marketDataMap = new HashMap<>();

        marketDataList.forEach(data -> {
            String key = data.getTICKER() + "_" + data.getEXCHANGE();
            data.setTICKER(key);
            marketDataMap.put(key, data);
        });

        natsService.publish(Event.MARKET_DATA_UPDATE, marketDataMap);

        marketDataList.forEach(System.out::println);

        this.saveToCache(marketDataMap);
    }

}
