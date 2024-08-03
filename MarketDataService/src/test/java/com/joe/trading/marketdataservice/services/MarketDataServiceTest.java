package com.joe.trading.marketdataservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.DAO.MarketDataRepo;
import com.joe.trading.marketdataservice.model.MarketData;
import com.joe.trading.shared.nats.NatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarketDataServiceTest {

    @Mock
    private MarketDataRepo mdRepo;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private NatsService natsService;

    @InjectMocks
    private MarketDataServiceImpl marketDataService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveToCacheTest() {
        MarketData marketData = new MarketData();
        marketData.setTICKER("IBM");
        marketDataService.saveToCache(marketData);
        verify(mdRepo, times(1))
                .saveMarketData(marketData, "IBM");
    }

    @Test
    void testSaveToCache_WithMapArgs() {
        Map<String, MarketData> testArg = new HashMap<>();
        MarketData marketData = new MarketData();
        marketData.setTICKER("AAPL");
        MarketData data2 = new MarketData();
        data2.setTICKER("IBM");

        testArg.put("AAPL", marketData);
        testArg.put("IBM", data2);

        marketDataService.saveToCache(testArg);
        verify(mdRepo, times(1))
                .saveAll(testArg);
    }

    @Test
    void getFromCacheTest() {
        MarketData data = new MarketData();
        data.setTICKER("AAPL");
        when(mdRepo.getMarketData("AAPL"))
                .thenReturn(data);

        MarketData result = marketDataService.getFromCache("AAPL");
        assertEquals(data, result);
    }

    @Test
    public void testGetFromCacheWithNull() {
        MarketData result = marketDataService.getFromCache(null);
        assertNull(result);
    }

    @Test
    void getAllFromCacheTest() {
        Map<String, MarketData> testMap = new HashMap<>();
        MarketData marketData = new MarketData();
        marketData.setTICKER("AAPL");
        MarketData data = new MarketData();
        data.setTICKER("ORCL");

        testMap.put("AAPL", marketData);
        testMap.put("ORCL", data);

        when(mdRepo.getAll()).thenReturn(testMap);

        Map<String, MarketData> result = marketDataService.getAllFromCache();
        assertEquals(testMap, result);
    }

    @Test
    void clearCacheTest() {
        marketDataService.clearCache();
        verify(mdRepo, times(1)).clearCache();
    }

    @Test
    void updateMarketDataTest() {
        MarketData data = new MarketData();
        marketDataService.updateMarketData(data);

        verify(mdRepo, times(1)).updateMarketData(data);
    }

    @Test
    void getMarketDataFromExchangeTest() {
        String exchange = "exchange1";
        String ticker = "AAPL";

        MarketData data = new MarketData();
        data.setTICKER("AAPL");

        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(data));

        MarketData result = marketDataService.getMarketDataFromExchange(exchange, ticker);

        assertNotNull(result);
        assertEquals(data, result);
    }

    @Test
    public void testGetMarketDataFromExchangeWithInvalidData() {
        String exchange = "exchange1";
        String ticker = "INVALID";
        when(restTemplate.getForEntity(anyString(), eq(Object.class))).thenReturn(ResponseEntity.notFound().build());
        MarketData result = marketDataService.getMarketDataFromExchange(exchange, ticker);
        assertNull(result);
    }

    @Test
    void testInitialSubscriptionCheck(){
        when(restTemplate.getForEntity(anyString(), eq(String[].class)))
                .thenReturn(ResponseEntity.ok(new String[]{}));
        marketDataService.initialSubscriptionCheck();
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(Boolean.class)
                );
    }

    @Test
    void buildInitialCacheEntryTest() throws JsonProcessingException {
        List<MarketData> marketDataList = Arrays.asList(new MarketData(), new MarketData());
        Object[] marketDataArray = marketDataList.toArray();

        when(restTemplate.getForEntity(anyString(), eq(Object[].class)))
                .thenReturn(ResponseEntity.ok(marketDataArray));

        doNothing().when(natsService).publish(any(), any());
        marketDataService.buildInitialCacheEntry();

        verify(mdRepo, times(1)).clearCache();
        verify(mdRepo, times(1)).saveAll(any());
    }
}