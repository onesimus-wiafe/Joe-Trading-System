package com.joe.trading.order_processing.services.validation;

import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.entities.User;
import com.joe.trading.order_processing.entities.dao.MarketDataDao;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.repositories.UserRepository;
import com.joe.trading.order_processing.services.validation.handler.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.joe.trading.order_processing.entities.enums.AvailableExchanges.*;

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

        List<MarketDataDao> marketData = getMarketDataFromExchange(orderRequest.getTicker(), orderRequest.getExchanges());


        return switch (Side.valueOf(orderRequest.getSide())){
            case BUY -> {
                ValidationHandler handler = new FundsValidator(user);
                handler.setNext(new QuantityValidator(Side.BUY, marketData));
                handler.setNext(new PriceValidator(Side.BUY));
                yield handler.validate(orderRequest);
            }
            case SELL -> {
                ValidationHandler handler = new OwnershipValidator(user);
                handler.setNext(new QuantityValidator(Side.SELL, marketData));
                handler.setNext(new PriceValidator(Side.SELL));
                yield handler.validate(orderRequest);
            }
        };
    }

    private List<MarketDataDao> getMarketDataFromExchange(String ticker, String exchanges) {
        return switch (AvailableExchanges.valueOf(exchanges.toUpperCase())){
            case EXCHANGE1 -> {
                MarketDataDao data = callExchange("https://exchange.matraining.com/pd/", ticker).getBody();
                assert data != null;
                data.setEXCHANGE(String.valueOf(EXCHANGE1));
                yield List.of(data);
            }
            case EXCHANGE2 -> {
                MarketDataDao data = callExchange("https://exchange2.matraining.com/pd/", ticker).getBody();
                assert data != null;
                data.setEXCHANGE(String.valueOf(EXCHANGE2));
                yield List.of(data);
            }
            case ALL -> {
                MarketDataDao data = callExchange("https://exchange.matraining.com/pd/", ticker).getBody();
                assert data != null;
                data.setEXCHANGE(String.valueOf(EXCHANGE1));
                MarketDataDao data1 = callExchange("https://exchange2.matraining.com/pd/", ticker).getBody();
                assert data1 != null;
                data1.setEXCHANGE(String.valueOf(EXCHANGE2));
                yield List.of(data, data1);
            }
            case NONE ->List.of( new MarketDataDao());
        };

    }

    private ResponseEntity<MarketDataDao> callExchange(String url, String ticker){

        String exchangeUrl = url + ticker.toUpperCase();

        return restTemplate.getForEntity(
                url, MarketDataDao.class);
    }
}
