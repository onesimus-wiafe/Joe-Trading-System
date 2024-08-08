package com.joe.trading.order_processing.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecutionsTest {
    @Test
    void testExecutionsCreation() {
        Executions executions = new Executions();
        executions.setId(1L);
        executions.setTimestamp("2024-07-23T10:15:30");
        executions.setPrice(100.0);
        executions.setQuantity(10);

        assertEquals(1L, executions.getId());
        assertEquals("2024-07-23T10:15:30", executions.getTimestamp());
        assertEquals(100.0, executions.getPrice());
        assertEquals(10, executions.getQuantity());
    }

    @Test
    void testExecutionsEquality() {
        Executions executions1 = new Executions();
        executions1.setId(1L);
        executions1.setTimestamp("2024-07-23T10:15:30");
        executions1.setPrice(100.0);
        executions1.setQuantity(10);

        Executions executions2 = new Executions();
        executions2.setId(1L);
        executions2.setTimestamp("2024-07-23T10:15:30");
        executions2.setPrice(100.0);
        executions2.setQuantity(10);

        assertEquals(executions1, executions2);
        assertEquals(executions1.hashCode(), executions2.hashCode());
    }

    @Test
    void testExecutionsToString() {
        Executions executions = new Executions();
        executions.setId(1L);
        executions.setTimestamp("2024-07-23T10:15:30");
        executions.setPrice(100.0);
        executions.setQuantity(10);

        String expected = "Executions{timestamp='2024-07-23T10:15:30', price=100.0, quantity=10}";
        assertTrue(executions.toString().contains(expected));
    }

    @Test
    void testSetOrderBook() {
        Executions executions = new Executions();
        OrderBook orderBook = new OrderBook();
        executions.setOrderBook(orderBook);
        assertEquals(orderBook, executions.getOrderBook());
    }
}