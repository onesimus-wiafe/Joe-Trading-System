package com.joe.trading.user_management.entites;

import com.joe.trading.user_management.entities.Portfolio;
import com.joe.trading.user_management.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testCreateUser(){
        User user = new User();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");

        assertEquals(1L,user.getId());
    }


    @Test
    void testUpdateUser(){
        User user = new User();
        user.setId(2L);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");

        user.setName("user2");
        assertEquals("user2",user.getName());
    }

    @Test
    void testUserCreatedAt(){
        User user = new User();
        user.setId(1L);
        user.setName("user2");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        assertEquals(now,user.getCreatedAt());
    }
    @Test
    void testAddUserPortfolio(){
        User user = new User();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");
        Portfolio p1 = new Portfolio();
        p1.setId(1L);
        p1.setName("T stocks");
        p1.setUser(user);
        var port_l = user.getPortfolios();
        System.out.println(port_l);


        assertNotNull(user.getPortfolios());
        //assertEquals(1,user.getPortfolios().size());
    }
}
