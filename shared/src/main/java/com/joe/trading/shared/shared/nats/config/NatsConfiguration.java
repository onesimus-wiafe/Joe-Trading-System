package com.joe.trading.shared.shared.nats.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.nats.client.Connection;
import io.nats.client.Nats;

@Configuration
public class NatsConfiguration {
    
    @Value("${nats.url}")
    private String natsUrl;

    @Bean
    public Connection natsConnection() throws IOException, InterruptedException {
        return Nats.connect(natsUrl);
    }
}
