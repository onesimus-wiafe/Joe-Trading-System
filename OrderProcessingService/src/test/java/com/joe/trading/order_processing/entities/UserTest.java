package com.joe.trading.order_processing.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void testUserEquality() {
        User user1 = new User(1L);
        User user2 = new User(1L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserToString() {
        User user = new User(1L);
        String expected = "User(id=1, totalFunds=null, portfolios=[], createdDate=null, updatedOn=null, accountType=null)";
        assertTrue(user.toString().contains(expected));
    }

    @Test
    void testAddPortfolio() {
        User user = new User(1L);
        Portfolio portfolio = new Portfolio("Tech Stocks");
        user.addPortfolio(portfolio);
        assertTrue(user.getPortfolios().contains(portfolio));
        assertEquals(user, portfolio.getUser());
    }

    @Test
    void testSetCreatedDate() {
        User user = new User(1L);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedDate(now);
        assertEquals(now, user.getCreatedDate());
    }

    @Test
    void testSetUpdatedOn() {
        User user = new User(1L);
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedOn(now);
        assertEquals(now, user.getUpdatedOn());
    }
}