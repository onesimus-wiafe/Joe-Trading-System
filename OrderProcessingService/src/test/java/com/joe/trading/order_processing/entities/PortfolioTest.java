package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.PortfolioState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortfolioTest {
    @Test
    void testPortfolioCreation() {
        Portfolio portfolio = new Portfolio("Tech Stocks");
        assertEquals("Tech Stocks", portfolio.getName());
        assertEquals(PortfolioState.ACTIVE, portfolio.getState());
        assertEquals(0.0, portfolio.getValue());
    }

    @Test
    void testDefaultPortfolioCreation(){
        Portfolio portfolio = new Portfolio("Default", PortfolioState.DEFAULT);
        assertEquals("Default", portfolio.getName());
        assertEquals(PortfolioState.DEFAULT, portfolio.getState());
    }

    @Test
    void testAddStock() {
        Portfolio portfolio = new Portfolio("Tech Stocks");
        Stock stock = new Stock();
        portfolio.addStock(stock);
        assertTrue(portfolio.getStocks().contains(stock));
    }

    @Test
    void testUpdateValue() {
        Portfolio portfolio = new Portfolio("Tech Stocks");
        portfolio.updateValue(100.0);
        assertEquals(100.0, portfolio.getValue());
    }

    @Test
    void testPortfolioEquality() {
        Portfolio portfolio1 = new Portfolio("Tech Stocks");
        Portfolio portfolio2 = new Portfolio("Tech Stocks");

        portfolio1.setId(1L);
        portfolio2.setId(1L);

        assertEquals(portfolio1, portfolio2);
        assertEquals(portfolio1.hashCode(), portfolio2.hashCode());
    }

    @Test
    void testPortfolioToString() {
        Portfolio portfolio = new Portfolio("Tech Stocks");
        portfolio.setId(1L);
        String expected = "Portfolio{state=ACTIVE, name='Tech Stocks', portfolioValue=0.0, stocks=[]}";
        assertTrue(portfolio.toString().contains(expected));
    }

    @Test
    void testSetUser() {
        Portfolio portfolio = new Portfolio("Tech Stocks");
        User user = new User(1L);
        portfolio.setUser(user);
        assertEquals(user, portfolio.getUser());
    }

}
