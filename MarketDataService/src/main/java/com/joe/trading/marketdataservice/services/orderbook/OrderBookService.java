package com.joe.trading.marketdataservice.services.orderbook;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderBookService {
    void publishOrderBook() throws JsonProcessingException;
    void publishOrderBook(String product) throws JsonProcessingException;
}
