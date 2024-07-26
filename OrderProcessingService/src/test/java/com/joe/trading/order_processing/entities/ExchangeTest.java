package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.ExchangeState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeTest {

    @Test
    void testExchangeCreation() {
        Exchange exchange = new Exchange("http://example.com", AvailableExchanges.EXCHANGE1);
        assertEquals("http://example.com", exchange.getUrl());
        assertEquals(AvailableExchanges.EXCHANGE1, exchange.getExchangeName());
        assertEquals(ExchangeState.SUBSCRIBED, exchange.getState());
    }

    @Test
    void testExchangeEquality() {
        Exchange exchange1 = new Exchange("http://example.com", AvailableExchanges.EXCHANGE1);
        Exchange exchange2 = new Exchange("http://example.com", AvailableExchanges.EXCHANGE1);
        assertEquals(exchange1, exchange2);
        assertEquals(exchange1.hashCode(), exchange2.hashCode());
    }

    @Test
    void testExchangeToString() {
        Exchange exchange = new Exchange("http://example.com", AvailableExchanges.EXCHANGE1);
        assertEquals("http://example.com", exchange.toString());
    }
}