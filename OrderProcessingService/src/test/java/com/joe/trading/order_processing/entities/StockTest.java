package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.Ticker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void testStockCreation() {
        Stock stock = new Stock("AAPL", 100);
        assertEquals(Ticker.AAPL, stock.getTicker());
        assertEquals(100, stock.getQuantity());
    }

    @Test
    void testUpdateStockValue() {
        Stock stock = new Stock("AAPL", 100);
        stock.setStockValue(1000.0);
        stock.updateStockValue(500.0);
        assertEquals(1500.0, stock.getStockValue());
    }

    @Test
    void testStockEquality() {
        Stock stock1 = new Stock("AAPL", 100);
        Stock stock2 = new Stock("AAPL", 100);

        stock1.setId(1L);
        stock2.setId(1L);

        assertEquals(stock1, stock2);
        assertEquals(stock1.hashCode(), stock2.hashCode());
    }

    @Test
    void testStockToString() {
        Stock stock = new Stock("AAPL", 100);
        stock.setId(1L);
        stock.setStockValue(1000.0);
        String expected = "Stock{ticker=AAPL, quantity=100, stockValue=1000.0}";
        assertTrue(stock.toString().contains(expected));
    }

    @Test
    void testSetPortfolio() {
        Stock stock = new Stock("AAPL", 100);
        Portfolio portfolio = new Portfolio("Tech Stocks");
        stock.setPortfolio(portfolio);
        assertEquals(portfolio, stock.getPortfolio());
    }

}