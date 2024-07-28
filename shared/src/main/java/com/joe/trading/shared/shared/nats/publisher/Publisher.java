package com.joe.trading.shared.shared.nats.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.shared.shared.nats.events.Event;

import io.nats.client.Connection;

public class Publisher {
    private Connection natsConnection;
    private ObjectMapper objectMapper;

    public void publish(Event event, Object data) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(data);
        natsConnection.publish(event.getValue(), message.getBytes());
    }
}
