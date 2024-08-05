package com.joe.trading.order_processing.services.validation;

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.repositories.UserRepository;
import com.joe.trading.order_processing.entities.cache.MarketData;
import com.joe.trading.order_processing.services.validation.handler.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.joe.trading.order_processing.entities.enums.AvailableExchanges.EXCHANGE1;
import static com.joe.trading.order_processing.entities.enums.AvailableExchanges.EXCHANGE2;

@Service
public class OrderValidationServiceImpl implements OrderValidationService{

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public OrderValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OrderRequestDTO validateOrder(OrderRequestDTO orderRequest) {
        orderRequest.setSide(orderRequest.getSide().toUpperCase());

        User user = userRepository.findById(orderRequest.getUserId()).orElse(null);

        // TODO: fetch market data from cache.
        List<MarketData> marketData = getMarketDataFromExchange(orderRequest.getTicker(), orderRequest.getExchanges());


        return switch (Side.valueOf(orderRequest.getSide())){
            case BUY -> {
                ValidationHandler fundHandler = new FundsValidator(user);
                ValidationHandler quantityHandler = new QuantityValidator(Side.BUY, marketData);
                ValidationHandler priceHandler = new PriceValidator(Side.BUY, marketData);

                fundHandler.setNext(quantityHandler);
                quantityHandler.setNext(priceHandler);

                yield fundHandler.validate(orderRequest);
            }
            case SELL -> {
                ValidationHandler ownHandler = new OwnershipValidator(user);
                ValidationHandler quantityHandler = new QuantityValidator(Side.SELL, marketData);
                ValidationHandler priceHandler = new PriceValidator(Side.SELL, marketData);

                ownHandler.setNext(quantityHandler);
                quantityHandler.setNext(priceHandler);

                yield ownHandler.validate(orderRequest);
            }
        };
    }

    private List<MarketData> getMarketDataFromExchange(String ticker, String exchanges) {
        return switch (AvailableExchanges.valueOf(exchanges.toUpperCase())){
            case EXCHANGE1 -> {
                MarketData data = callExchange("https://exchange.matraining.com/pd/", ticker);
                data.setEXCHANGE(String.valueOf(EXCHANGE1));
                yield List.of(data);
            }
            case EXCHANGE2 -> {
                MarketData data = callExchange("https://exchange2.matraining.com/pd/", ticker);
                data.setEXCHANGE(String.valueOf(EXCHANGE2));
                yield List.of(data);
            }
            case ALL -> {
                MarketData data = callExchange("https://exchange.matraining.com/pd/", ticker);
                data.setEXCHANGE(String.valueOf(EXCHANGE1));
                MarketData data1 = callExchange("https://exchange2.matraining.com/pd/", ticker);
                data1.setEXCHANGE(String.valueOf(EXCHANGE2));
                yield List.of(data, data1);
            }
            case NONE ->List.of( new MarketData());
        };

    }

    // TODO: MOVE TO MARKET DATA SERVICE
    private MarketData callExchange(String url, String ticker){

        String exchangeUrl = url + ticker.toUpperCase();

        ResponseEntity<Object> response = restTemplate.getForEntity(
                exchangeUrl, Object.class);

        Object obj = response.getBody();

        MarketData data = new MarketData();

        if (obj instanceof Map){
            Map<String, Object> dataMap = (Map<String, Object>) obj;

            data.setLAST_TRADED_PRICE((Double) dataMap.get("LAST_TRADED_PRICE"));
            data.setTICKER((String) dataMap.get("TICKER"));
            data.setSELL_LIMIT((Integer) dataMap.get("SELL_LIMIT"));
            data.setBID_PRICE((Double) dataMap.get("BID_PRICE"));
            data.setBUY_LIMIT((Integer) dataMap.get("BUY_LIMIT"));
            data.setASK_PRICE((Double) dataMap.get("ASK_PRICE"));
            data.setMAX_PRICE_SHIFT(Double.valueOf((Integer) (dataMap.get("MAX_PRICE_SHIFT"))));
        }

        return data;
    }
}
