package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.OrderType;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.enums.Ticker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    @Test
    void testOrderCreation() {
        Order order = new Order(Ticker.AAPL, 100, 1.0, Side.BUY, AvailableExchanges.EXCHANGE1, OrderType.LIMIT);
        assertEquals(1.0, order.getUnitPrice());
        assertEquals(100, order.getQuantity());
        assertEquals(Side.BUY, order.getSide());
        assertEquals(OrderType.LIMIT, order.getOrderType());
        assertEquals(Ticker.AAPL, order.getTicker());
    }

    @Test
    void testOrderToString() {
        Order order = new Order(Ticker.AAPL, 100, 1.0, Side.BUY, AvailableExchanges.EXCHANGE1, OrderType.LIMIT);
        String expected = "Order{ticker=AAPL, quantity=100, unitPrice=1.0, side=BUY, exchanges=EXCHANGE1, orderType=LIMIT, trades=null}";
        assertTrue(order.toString().contains(expected));
    }
}