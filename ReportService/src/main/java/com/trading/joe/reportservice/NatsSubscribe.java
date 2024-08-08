package com.trading.joe.reportservice;

import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.trading.joe.reportservice.entities.MarketData;
import com.trading.joe.reportservice.repository.MarketDataRepository;
import com.trading.joe.reportservice.repository.OrderBookRepository;
import jakarta.annotation.PostConstruct;

import java.util.Map;

public class NatsSubscribe {
    private NatsService natsService;
    private OrderBookRepository orderBookRepository;
    private MarketDataRepository marketDataRepository;

    @PostConstruct
    public void subscribeToMarketDataUpdate(){
        natsService.subscribe(Event.MARKET_DATA_UPDATE, Map.class, this::saveMarketDataUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subScribeToOrderBookUpdateForIBM(){
        natsService.subscribe(Event.IBM_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForAAPL(){
        natsService.subscribe(Event.AAPL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForAMZN(){
        natsService.subscribe(Event.AMZN_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForMSFT(){
        natsService.subscribe(Event.MSFT_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
    }

    @PostConstruct
    public void subscribeToOrderBookForNFLX(){
        natsService.subscribe(Event.NFLX_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
    }

    @PostConstruct
    public void subscribeToOrderBookForORCL(){
        natsService.subscribe(Event.ORCL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
    }

    @PostConstruct
    public void subscribeToOrderBookForTSLA(){
        natsService.subscribe(Event.TSLA_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
    }

    @PostConstruct
    public void subscribeToOrderBookForGOOGL(){
        natsService.subscribe(Event.GOOGL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate);
    }

    private void saveOrderBookUpdate(Map map){
        var order_book = map.values();
        orderBookRepository.save(order_book.get);
    }

    private void marketDataDto(Map map){
        MarketData marketData = new MarketData();
        marketData.setId(map.);
    }

}
