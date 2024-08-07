package com.joe.trading.order_processing.nats;

import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.entities.cache.MarketData;
import com.joe.trading.order_processing.repositories.redis.dao.InternalOpenOrderDAO;
import com.joe.trading.order_processing.repositories.redis.dao.MarketDataDAO;
import com.joe.trading.order_processing.repositories.redis.dao.OrderBookDAO;
import com.joe.trading.order_processing.services.OrderBookService;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NatsSubscriber {
    private final NatsService natsService;
    private final MarketDataDAO marketDataRepo;
    private final InternalOpenOrderDAO openOrderRepo;
    private final OrderBookDAO orderBookRepo;
    private final OrderBookService orderBookService;

    @Autowired
    public NatsSubscriber(NatsService natsService, MarketDataDAO marketDataRepo, InternalOpenOrderDAO openOrderRepo, OrderBookDAO orderBookRepo, OrderBookService orderBookService) {
        this.natsService = natsService;
        this.marketDataRepo = marketDataRepo;
        this.openOrderRepo = openOrderRepo;
        this.orderBookRepo = orderBookRepo;
        this.orderBookService = orderBookService;
    }

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
        natsService.subscribe(Event.MSFT_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForNFLX(){
        natsService.subscribe(Event.NFLX_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForORCL(){
        natsService.subscribe(Event.ORCL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForTSLA(){
        natsService.subscribe(Event.TSLA_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForGOOGL(){
        natsService.subscribe(Event.GOOGL_ORDER_BOOK, Map.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    private void saveMarketDataUpdate(Map<Object, Object> message) {
        Map<String, MarketData> map = new HashMap<>();

        message.forEach((k, v) -> {
            String key = (String) k;
            MarketData value = (MarketData) v;
            map.put(key, value);
        });

        List<String> keys = map.keySet().stream().toList();

        if (keys.size() > 1){
            this.marketDataRepo.saveAll(map);
        }
        else
        {
            String key = keys.get(0);
            this.marketDataRepo.saveMarketData(map.get(key), key);
        }
    }

    private void saveOrderBookUpdate(Map<Object, Object> message){
        Map<String, List<OrderBook>> actualMapData = new HashMap<>();

        message.forEach((k, v) -> {
            String key = (String) k;
            List<OrderBook> values = new ArrayList<>();

            List<Object> data = (List<Object>) v;

            data.forEach(book -> {
                OrderBook value = (OrderBook) book;

                values.add(value);
            });

            actualMapData.put(key, values);
        });

        List<String> keys = actualMapData.keySet().stream().toList();

        keys.forEach(key -> {

            if (key.contains("OPEN")){

                List<OrderBook> booksToCache = updateInternalOrders(key, key, actualMapData);
                orderBookRepo.saveOrderBook(key, booksToCache);
            }
            else {
                int key_len = key.length();
                String cacheKey = key.substring(0, key_len-5)+"_OPEN";
                updateInternalOrders(cacheKey, key, actualMapData);
            }
        });

     }

     private List<OrderBook> updateInternalOrders(String cacheKey, String key, Map<String, List<OrderBook>> actualMapData){
         List<String> openOrders = openOrderRepo.getInternalOrder(cacheKey);

         List<OrderBook> internalOrders = new ArrayList<>();
         List<OrderBook> orderBooksForKey = actualMapData.get(key);

         orderBooksForKey.forEach(book -> {
             if (openOrders.contains(book.getId())){
                 internalOrders.add(book);
             }
         });

         if (!internalOrders.isEmpty()){
             orderBookService.updateOrderBooks(internalOrders);
         }

         return orderBooksForKey.stream().filter(book -> !internalOrders.contains(book)).toList();
     }
}
