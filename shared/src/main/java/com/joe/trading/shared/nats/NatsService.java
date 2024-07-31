package com.joe.trading.shared.nats;

import java.io.IOException;
import java.util.function.Consumer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.shared.events.Event;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

@Service
@EnableConfigurationProperties(NatsProperties.class)
public class NatsService {

    private Connection natsConnection;
    private ObjectMapper objectMapper;

    public NatsService(ObjectMapper objectMapper, NatsProperties natsProperties) throws IOException, InterruptedException {
        this.objectMapper = objectMapper;
        natsConnection = Nats.connect(natsProperties.getUrl());
    }

    public void publish(Event event, Object data) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(data);
        natsConnection.publish(event.getValue(), message.getBytes());
    }

    public <T> void subscribe(Event event, Class<T> eventType, Consumer<T> handler) {
        subscribe(event, eventType, handler, null);
    }

    public <T> void subscribe(Event event, Class<T> eventType, Consumer<T> handler, String queueGroup) {
        Dispatcher dispatcher = natsConnection.createDispatcher(msg -> {
            try {
                T message = objectMapper.readValue(msg.getData(), eventType);
                handler.accept(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (queueGroup != null) {
            dispatcher.subscribe(event.getValue(), queueGroup);
        } else {
            dispatcher.subscribe(event.getValue());
        }
    }
}
