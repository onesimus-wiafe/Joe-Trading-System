package com.joe.trading.user_management.entites;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.joe.trading.user_management.entities.Portfolio;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.enums.AccountType;
import com.joe.trading.user_management.repository.PortfolioRepository;
import com.joe.trading.user_management.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserTest {

    @Autowired
    private EntityManager entityManager;

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

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.delete(user);
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

        //user.setPortfolios(List.of(p1));

        portfolioRepository.save(p1);

        User aSave = userRepository.findByEmail(user.getEmail()).get();

        System.out.println(user.getPortfolios().size());

        List<Portfolio> ps = portfolioRepository.findAll();

        assertEquals(1, portfolioRepository.count());

        portfolioRepository.delete(p1);
        portfolioRepository.flush();

        assertEquals(0, portfolioRepository.count());  // delete portfolio
        System.out.println(user.getId());
        System.out.println(user.getPortfolios().size());
        User user1 = userRepository.findByEmail("test@email.com").get();
        System.out.println(p1);
        //assertEquals(0, user1.getPortfolios().size());   //user has no portfolio
        userRepository.delete(user);
        assertEquals(0,userRepository.count());
    }
}
