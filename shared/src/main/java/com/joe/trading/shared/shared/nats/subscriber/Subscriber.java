package com.joe.trading.shared.shared.nats.subscriber;

import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.shared.shared.nats.events.Event;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;

public class Subscriber {
    private Connection natsConnection;
    private ObjectMapper objectMapper;

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
