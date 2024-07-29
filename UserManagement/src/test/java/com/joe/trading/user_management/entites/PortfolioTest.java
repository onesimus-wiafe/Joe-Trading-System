package com.joe.trading.user_management.entites;

import com.joe.trading.user_management.entities.Portfolio;
import com.joe.trading.user_management.entities.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {

    @Test
    void testCreatePortfolio(){
        User user1 = new User();
        user1.setId(1L);
        user1.setName("user1");
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setName("Techies");
        portfolio.setUser(user1);

        assertNotNull(portfolio);

    }
}
