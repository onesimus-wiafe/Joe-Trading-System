package com.joe.trading.order_processing.services.validation.handler;

import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.cache.MarketData;

import java.util.List;

public class QuantityValidator extends ValidationHandler{

    private Side side;
    private List<MarketData> marketData;

    public QuantityValidator(Side side, List<MarketData> data){
        super();
        this.side = side;
        this.marketData = data;
    }

    @Override
    public OrderRequestDTO validate(OrderRequestDTO orderRequestDTO) {
        if (marketData.isEmpty()){
            super.setNext(null);
            orderRequestDTO.setIsValidated(Boolean.FALSE);
            return super.validate(orderRequestDTO);
        }
        return super.validate(validateQuantity(orderRequestDTO));
    }

    private OrderRequestDTO validateQuantity(OrderRequestDTO request){

        List<MarketData> filter = marketData.stream().filter(data -> switch (this.side) {
            case SELL -> data.getSELL_LIMIT() < request.getQuantity();
            case BUY -> data.getBUY_LIMIT() < request.getQuantity();
        }).toList();

        if (filter.isEmpty()){
            super.setNext(null);
            request.setIsValidated(Boolean.FALSE);
            return request;
        } else if (filter.size() == 1) {
            request.setExchanges(filter.get(0).getEXCHANGE());
            request.setIsValidated(Boolean.TRUE);
            return request;
        }
        else{
            request.setExchanges("BOTH");
            request.setIsValidated(Boolean.TRUE);
            return request;
        }
    }
}
