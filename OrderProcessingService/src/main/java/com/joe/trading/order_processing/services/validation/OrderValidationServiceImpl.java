package com.joe.trading.order_processing.services.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.cache.MarketData;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.repositories.jpa.UserRepository;
import com.joe.trading.order_processing.repositories.redis.dao.MarketDataDAO;
import com.joe.trading.order_processing.services.validation.handler.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.joe.trading.order_processing.entities.enums.AvailableExchanges.EXCHANGE1;
import static com.joe.trading.order_processing.entities.enums.AvailableExchanges.EXCHANGE2;

@Service
public class OrderValidationServiceImpl implements OrderValidationService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final MarketDataDAO marketDataRepo;

    public OrderValidationServiceImpl(UserRepository userRepository, RestTemplate restTemplate,
            MarketDataDAO marketDataRepo) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.marketDataRepo = marketDataRepo;
    }

    @Override
    public OrderRequestDTO validateOrder(Long userId, OrderRequestDTO orderRequest) {
        orderRequest.setSide(orderRequest.getSide().toUpperCase());

        User user = userRepository.findById(userId).orElse(null);

        List<MarketData> marketData = getMarketDataFromCache(orderRequest.getTicker(), orderRequest.getExchanges());

        if (marketData.isEmpty()) {
            marketData = getMarketDataFromExchange(orderRequest.getTicker(), orderRequest.getExchanges());
        }

        Side orderSide = Side.valueOf(orderRequest.getSide());

        ValidationHandler quantityHandler = new QuantityValidator(orderSide, marketData);
        ValidationHandler priceHandler = new PriceValidator(orderSide, marketData);

        return switch (orderSide) {
            case BUY -> {
                ValidationHandler fundHandler = new FundsValidator(user);

                fundHandler.setNext(quantityHandler);
                quantityHandler.setNext(priceHandler);

                yield fundHandler.validate(orderRequest);
            }
            case SELL -> {
                ValidationHandler ownHandler = new OwnershipValidator(user);

                ownHandler.setNext(quantityHandler);
                quantityHandler.setNext(priceHandler);

                yield ownHandler.validate(orderRequest);
            }
        };
    }

    private List<MarketData> getMarketDataFromExchange(String ticker, String exchanges) {
        String url = "https://exchange.matraining.com/pd/";
        String url2 = "https://exchange2.matraining.com/pd/";

        return switch (AvailableExchanges.valueOf(exchanges.toUpperCase())) {
            case EXCHANGE1 -> {
                MarketData data = callExchange(url, ticker);
                data.setEXCHANGE(String.valueOf(EXCHANGE1));
                var list = new ArrayList<MarketData>();
                list.add(data);
                yield list;
            }
            case EXCHANGE2 -> {
                MarketData data = callExchange(url2, ticker);
                data.setEXCHANGE(String.valueOf(EXCHANGE2));
                var list = new ArrayList<MarketData>();
                list.add(data);
                yield list;
            }
            case ALL -> {
                MarketData data = callExchange(url, ticker);
                data.setEXCHANGE(String.valueOf(EXCHANGE1));
                MarketData data1 = callExchange(url2, ticker);
                data1.setEXCHANGE(String.valueOf(EXCHANGE2));
                var list = new ArrayList<MarketData>();
                list.add(data);
                list.add(data1);
                yield list;
            }
            case NONE -> {
                yield new ArrayList<MarketData>();
            }
        };
    }

    private List<MarketData> getMarketDataFromCache(String ticker, String exchanges) {
        return switch (AvailableExchanges.valueOf(exchanges.toUpperCase())) {
            case EXCHANGE1 -> {
                List<MarketData> list = new ArrayList<>();
                MarketData data = this.marketDataRepo.getMarketData(ticker + "_EX1").orElse(null);
                if (data != null) {
                    list.add(data);
                }
                yield list;
            }
            case EXCHANGE2 -> {
                List<MarketData> list = new ArrayList<>();
                MarketData data = this.marketDataRepo.getMarketData(ticker + "_EX2").orElse(null);
                if (data != null) {
                    list.add(data);
                }
                yield list;
            }
            case NONE -> new ArrayList<MarketData>();
            case ALL -> {
                List<MarketData> data = getMarketDataFromCache(ticker, "EXCHANGE1");
                data.addAll(getMarketDataFromCache(ticker, "EXCHANGE2"));

                yield data;
            }
        };
    }

    private MarketData callExchange(String url, String ticker) {

        String exchangeUrl = url + ticker.toUpperCase();

        ResponseEntity<Object> response = restTemplate.getForEntity(
                exchangeUrl, Object.class);

        Object object = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        MarketData data = new MarketData();
        try {
            data = objectMapper.convertValue(object, MarketData.class);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
