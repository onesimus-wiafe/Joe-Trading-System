package com.joe.trading.user_management.entites;

import com.joe.trading.user_management.entities.User;
import org.junit.jupiter.api.Test;

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

}
