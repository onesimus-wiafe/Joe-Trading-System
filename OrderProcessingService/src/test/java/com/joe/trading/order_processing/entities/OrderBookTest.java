package com.joe.trading.order_processing.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderBookTest {

    @Test
    void testOrderBookCreation() {
        OrderBook orderBook = new OrderBook("BTC-USD", "1", 100, "BUY", "LIMIT");
        assertEquals("1", orderBook.getId());
        assertEquals("BTC-USD", orderBook.getProduct());
        assertEquals(100, orderBook.getQuantity());
        assertEquals("BUY", orderBook.getSide());
        assertEquals("LIMIT", orderBook.getOrderType());
    }

    @Test
    void testAddExecution() {
        OrderBook orderBook = new OrderBook("BTC-USD", "1", 100, "BUY", "LIMIT");
        Executions execution = new Executions();
        execution.setOrderBook(orderBook);
        orderBook.getExecutions().add(execution);
        assertEquals(1, orderBook.getExecutions().size());
    }

    @Test
    void testOrderBookEquality() {
        OrderBook orderBook1 = new OrderBook("BTC-USD", "1", 100, "BUY", "LIMIT");
        OrderBook orderBook2 = new OrderBook("BTC-USD", "1", 100, "BUY", "LIMIT");
        assertEquals(orderBook1, orderBook2);
        assertEquals(orderBook1.hashCode(), orderBook2.hashCode());
    }

    @Test
    void testOrderBookToString() {
        OrderBook orderBook = new OrderBook("BTC-USD", "1", 100, "BUY", "LIMIT");
        String expected = "OrderBook{price=null, product='BTC-USD', quantity=100, cumulatitiveQuantity=0, side='BUY', type='LIMIT', executions=[], trade=null}";
        assertTrue(orderBook.toString().contains(expected));
    }
}