package com.joe.trading.marketdataservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.shared.nats.NatsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class NatsServiceConfig {
    
    @Bean
    public NatsService natsService() throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        return new NatsService(mapper);
    }
}
