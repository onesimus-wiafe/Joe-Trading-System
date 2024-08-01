package com.joe.trading.marketdataservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.marketdataservice.DAO.MarketDataRepo;
import com.joe.trading.marketdataservice.model.MarketData;
import com.joe.trading.marketdataservice.services.enums.Exchange;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final MarketDataRepo repo;
    private final RestTemplate restTemplate;
    private final NatsService natsService;

    private final String exchange1Url = "https://exchange.matraining.com";
    private final String exchange2Url = "https://exchange2.matraining.com";

    @Autowired
    public MarketDataServiceImpl(MarketDataRepo repo, RestTemplate restTemplate, NatsService natsService){
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
        String endOfUrl = "/pd/"+ ticker.toUpperCase();
        return switch (Exchange.valueOf(exchange)){
            case EXCHANGE1 -> this.getOneFromExchange(exchange1Url+endOfUrl);
            case EXCHANGE2 -> this.getOneFromExchange(exchange2Url+endOfUrl);
        };
    }

    private List<MarketData> getAllMarketDataFromExchange(String exchange){
        String endOfUrl = "/pd";
        return switch (Exchange.valueOf(exchange.toUpperCase())){
            case EXCHANGE1 -> this.getFromExchange(exchange1Url+endOfUrl, "EX1");
            case EXCHANGE2 -> this.getFromExchange(exchange2Url+endOfUrl, "EX2");

        };
    }

    private List<MarketData> getFromExchange(String url, String exchange){
        ResponseEntity<Object[]> response = restTemplate.getForEntity(
                url, Object[].class
        );

        List<MarketData> result = Arrays.stream(Objects.requireNonNull(response.getBody())).map(
                this::mapObject).toList();

        result.forEach(data -> data.setEXCHANGE(exchange));
        return result;
    }

    private MarketData getOneFromExchange(String url){
        ResponseEntity<Object> response = restTemplate.getForEntity(
                url, Object.class
        );
        return mapObject(response.getBody());
    }

    private MarketData mapObject(Object object){
        ObjectMapper objectMapper = new ObjectMapper();

        MarketData data;
        try {
            data = objectMapper.convertValue(object, MarketData.class);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
