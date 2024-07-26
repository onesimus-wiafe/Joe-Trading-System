package com.joe.trading.order_processing.entities.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketDataDao {

    private String TICKER;
    private Integer SELL_LIMIT;
    private Double LAST_TRADE_PRICE;
    private Double MAX_PRICE_SHIFT;
    private Double ASK_PRICE;
    private Double BID_PRICE;
    private Integer BUY_LIMIT;
    private String EXCHANGE;

}
