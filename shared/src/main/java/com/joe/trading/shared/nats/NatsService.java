package com.joe.trading.shared.nats;

import java.io.IOException;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.shared.events.Event;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

@Service
@ConditionalOnProperty(value = "nats.enabled", havingValue = "true", matchIfMissing = true)
public class NatsService {

    @Value("${nats.url}")
    private String natsUrl;

    private Connection natsConnection;
    private ObjectMapper objectMapper;

    public NatsService(ObjectMapper objectMapper) throws IOException, InterruptedException {
        this.objectMapper = objectMapper;
        natsConnection = Nats.connect(natsUrl);
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
