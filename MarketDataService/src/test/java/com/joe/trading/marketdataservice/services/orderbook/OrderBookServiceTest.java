package com.joe.trading.marketdataservice.services.orderbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.marketdataservice.model.OrderBook;
import com.joe.trading.shared.nats.NatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderBookServiceTest {

    @Mock
    private NatsService natsService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderBookServiceImpl orderBookService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublishOrderBook() throws JsonProcessingException {
        doNothing().when(natsService).publish(any(), any());

        List<com.joe.trading.shared.dtos.OrderBook> orderBooks = new ArrayList<>();
        orderBooks.add(new com.joe.trading.shared.dtos.OrderBook());
        orderBooks.add(new com.joe.trading.shared.dtos.OrderBook());

        com.joe.trading.shared.dtos.OrderBook[] orderBookArray = new com.joe.trading.shared.dtos.OrderBook[orderBooks.size()];
        int count = 0;

        while (count < orderBooks.size()){
            orderBookArray[count] = orderBooks.get(count);
            count += 1;;
        }

        when(restTemplate.getForEntity(anyString(), eq(com.joe.trading.shared.dtos.OrderBook[].class)))
                .thenReturn(ResponseEntity.ok(orderBookArray));

        orderBookService.publishOrderBook();

        verify(natsService, times(32)).publish(any(), any());

    }
}
