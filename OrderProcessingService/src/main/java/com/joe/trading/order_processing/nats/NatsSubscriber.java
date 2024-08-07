package com.joe.trading.order_processing.nats;

import com.joe.trading.order_processing.entities.cache.MarketData;
import com.joe.trading.order_processing.repositories.redis.dao.MarketDataDAO;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NatsSubscriber {
    private final NatsService natsService;
    private final MarketDataDAO marketDataRepo;

    @Autowired
    public NatsSubscriber(NatsService natsService, MarketDataDAO marketDataRepo) {
        this.natsService = natsService;
        this.marketDataRepo = marketDataRepo;
    }

    @PostConstruct
    public void subscribeToMarketDataUpdate(){
        natsService.subscribe(Event.MARKET_DATA_UPDATE, Map.class, this::saveUpdate, "OrderProcessing");
    }

    private void saveUpdate(Map<Object, Object> message) {
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

    // TODO: Subscribe to each event you have - Save Only open.
}
