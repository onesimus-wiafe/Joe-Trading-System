package com.joe.trading.order_processing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderProcessingServiceApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    @Disabled("Skipping contextLoads test")
    void contextLoads() {
        assertThat(dataSource).isNotNull();
    }

}
