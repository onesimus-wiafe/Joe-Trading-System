package com.joe.trading.order_processing.services.validation.handler;

import com.joe.trading.order_processing.entities.cache.MarketData;
import com.joe.trading.order_processing.entities.dto.OrderRequestDTO;
import com.joe.trading.order_processing.entities.enums.Side;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class PriceValidator extends ValidationHandler {
    private final Side side;
    private final List<MarketData> marketData;

    public PriceValidator(Side side, List<MarketData> marketData){
        super();
        this.side = side;
        this.marketData = marketData;
    }

    @Override
    public OrderRequestDTO validate(OrderRequestDTO orderRequestDTO) {
        if (marketData.isEmpty()){
            super.setNext(null);
            orderRequestDTO.setIsValidated(FALSE);
            return orderRequestDTO;
        }
        return super.validate(validatePrice(orderRequestDTO));
    }

    public OrderRequestDTO validatePrice(OrderRequestDTO request){
        List<MarketData> filter = marketData.stream().filter(
                data -> switch (this.side){
                    case BUY ->
                            (Math.abs(data.getBID_PRICE()) - request.getUnitPrice()) < data.getMAX_PRICE_SHIFT();
                    case SELL -> Math.abs(data.getASK_PRICE() - request.getUnitPrice()) < data.getMAX_PRICE_SHIFT();
                }
        ).toList();

        if (filter.isEmpty()){
            super.setNext(null);
            request.setIsValidated(Boolean.FALSE);
            return request;
        }
        else if (filter.size() == 1){
            request.setExchanges(filter.get(0).getEXCHANGE());
            request.setIsValidated(TRUE);
            return request;
        }else {
            request.setIsValidated(TRUE);
            request.setExchanges("BOTH");
            return request;
        }
    }
}
