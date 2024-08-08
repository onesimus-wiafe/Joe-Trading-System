package com.joe.trading.order_processing.nats;

import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.entities.cache.MarketData;
import com.joe.trading.order_processing.mappers.OrderBookMapper;
import com.joe.trading.order_processing.mappers.UserMapper;
import com.joe.trading.order_processing.repositories.jpa.UserRepository;
import com.joe.trading.order_processing.repositories.redis.dao.InternalOpenOrderDAO;
import com.joe.trading.order_processing.repositories.redis.dao.MarketDataDAO;
import com.joe.trading.order_processing.repositories.redis.dao.OrderBookDAO;
import com.joe.trading.order_processing.services.OrderBookService;
import com.joe.trading.order_processing.services.PortfolioService;
import com.joe.trading.shared.dtos.OrderBookUpdateDto;
import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class NatsSubscriber {
    private final NatsService natsService;
    private final MarketDataDAO marketDataRepo;
    private final InternalOpenOrderDAO openOrderRepo;
    private final OrderBookDAO orderBookRepo;
    private final OrderBookService orderBookService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PortfolioService portfolioService;
    private final OrderBookMapper orderBookMapper;

    @PostConstruct
    public void subscribeToUserCreation(){
        natsService.subscribe(Event.USER_CREATED, UserEventDto.class, this::saveUser, "OrderProcessing");
    }

    private void saveUser(UserEventDto userEventDto){
        userRepository.save(userMapper.mapToUser(userEventDto));
        portfolioService.createDefaultPortolio(userEventDto.getId());
    }

    @PostConstruct
    public void subscribeToMarketDataUpdate(){
        natsService.subscribe(Event.MARKET_DATA_UPDATE, Map.class, this::saveMarketDataUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subScribeToOrderBookUpdateForIBM(){
        natsService.subscribe(Event.IBM_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForAAPL(){
        natsService.subscribe(Event.AAPL_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForAMZN(){
        natsService.subscribe(Event.AMZN_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForMSFT(){
        natsService.subscribe(Event.MSFT_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForNFLX(){
        natsService.subscribe(Event.NFLX_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForORCL(){
        natsService.subscribe(Event.ORCL_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForTSLA(){
        natsService.subscribe(Event.TSLA_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
    }

    @PostConstruct
    public void subscribeToOrderBookForGOOGL(){
        natsService.subscribe(Event.GOOGL_ORDER_BOOK, OrderBookUpdateDto.class, this::saveOrderBookUpdate, "OrderProcessing");
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

    private void saveOrderBookUpdate(OrderBookUpdateDto message){
        List<com.joe.trading.shared.dtos.OrderBook> orderBooks = message.getOrderBooks();
        String key = message.getSourceExchange();

        if (key.contains("OPEN")){

            List<OrderBook> booksToCache = updateInternalOrders(key, key, orderBooks);
            orderBookRepo.saveOrderBook(key, booksToCache);
        }
        else {
            int keyLen = key.length();
            String cacheKey = key.substring(0, keyLen-5)+"_OPEN";
            updateInternalOrders(cacheKey, key, orderBooks);
        }

     }

    private List<OrderBook> updateInternalOrders(String cacheKey, String key, List<com.joe.trading.shared.dtos.OrderBook> books){
         List<String> openOrders = openOrderRepo.getInternalOrder(cacheKey);

         List<OrderBook> internalOrders = new ArrayList<>();

         List<OrderBook> orderBooksForKey = books.stream()
                 .map(orderBookMapper::mapToOrderBook).toList();

         if (openOrders == null){
             return orderBooksForKey;
         }

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
