package com.joe.trading.user_management.entites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.user_management.entities.Portfolio;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.repository.PortfolioRepository;
import com.joe.trading.user_management.repository.UserRepository;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Test
    @Transactional
    void testAddUserPortfolio() {
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");
        user.setAccountType(AccountType.USER);
        user.setPendingDelete(false);

        Portfolio p1 = new Portfolio();
        p1.setName("T stocks");
        p1.setUser(user);

        user.setPortfolios(List.of(p1));

        userRepository.save(user);

        user = userRepository.getReferenceById(user.getId());

        assertNotNull(user.getPortfolios());
        assertEquals(1, user.getPortfolios().size());
        assertEquals(p1.getUser().getId(), user.getId());
    }

    @Test
    @Transactional
    void testDeleteUserWithPortfolio() {
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@email.com");
        user.setPasswordHash("password");
        user.setAccountType(AccountType.USER);
        user.setPendingDelete(false);

        Portfolio p1 = new Portfolio();
        p1.setName("T stocks");
        p1.setUser(user);

        user.setPortfolios(List.of(p1));

        userRepository.save(user);
        portfolioRepository.save(p1);

        assertEquals(1, userRepository.count());
        assertEquals(1, portfolioRepository.count());

        userRepository.delete(user);
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.flush();
        });
    }

    @Test
    @Transactional
    void testDeleteUserWithoutPortfolio() {
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@email.com");
        user.setPasswordHash("password");
        user.setAccountType(AccountType.USER);
        user.setPendingDelete(false);
        userRepository.save(user);

        Portfolio p1 = new Portfolio();
        p1.setName("T stocks");
        p1.setUser(user);

        user.setPortfolios(List.of(p1));

        portfolioRepository.save(p1);

        assertEquals(1, portfolioRepository.count());
        assertEquals(1, userRepository.count());

        portfolioRepository.delete(p1);
        portfolioRepository.flush();

        assertEquals(0, portfolioRepository.count()); // delete portfolio

        userRepository.delete(user);
        assertEquals(0, userRepository.count());
    }
}
