package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.TradeStatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {
    @Test
    void testTradeCreation() {
        Trade trade = new Trade();
        trade.setQuantity(100);
        trade.setPrice(1500.0);
        trade.setTicker("AAPL");
        trade.setSide("BUY");
        trade.setTradeType("LIMIT");
        trade.setStatus(TradeStatus.OPEN);

        assertEquals(100, trade.getQuantity());
        assertEquals(1500.0, trade.getPrice());
        assertEquals("AAPL", trade.getTicker());
        assertEquals("BUY", trade.getSide());
        assertEquals("LIMIT", trade.getTradeType());
        assertEquals(TradeStatus.OPEN, trade.getStatus());
    }

    @Test
    void testTradeEquality() {
        Trade trade1 = new Trade();
        trade1.setId(1L);
        trade1.setQuantity(100);
        trade1.setPrice(1500.0);

        Trade trade2 = new Trade();
        trade2.setId(1L);
        trade2.setQuantity(100);
        trade2.setPrice(1500.0);

        assertEquals(trade1, trade2);
        assertEquals(trade1.hashCode(), trade2.hashCode());
    }

    @Test
    void testTradeToString() {
        Trade trade = new Trade();
        trade.setId(1L);
        trade.setQuantity(100);
        trade.setPrice(1500.0);
        trade.setTicker("AAPL");
        trade.setSide("BUY");

        String expected = "Trade{quantity=100, price=1500.0, ticker='AAPL', side='BUY', tradeType='null', status=null, order=null, orderBook=null}";
        assertTrue(trade.toString().contains(expected));
    }

    @Test
    void testSetOrder() {
        Trade trade = new Trade();
        Order order = new Order();
        trade.setOrder(order);
        assertEquals(order, trade.getOrder());
    }

    @Test
    void testSetOrderBook() {
        Trade trade = new Trade();
        OrderBook orderBook = new OrderBook();
        trade.setOrderBook(orderBook);
        assertEquals(orderBook, trade.getOrderBook());
    }
}