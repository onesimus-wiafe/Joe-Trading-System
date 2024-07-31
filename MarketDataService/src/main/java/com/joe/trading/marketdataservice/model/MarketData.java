package com.joe.trading.marketdataservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MarketData {
    private Double LAST_TRADED_PRICE;
    private String TICKER;
    private Integer SELL_LIMIT;
    private Double BID_PRICE;
    private Integer BUY_LIMIT;

    private Double ASK_PRICE;
    private Double MAX_PRICE_SHIFT;
    private String EXCHANGE;

    @Override
    public String toString() {
        return "MarketData{" +
                "LAST_TRADED_PRICE=" + LAST_TRADED_PRICE +
                ", TICKER='" + TICKER + '\'' +
                ", SELL_LIMIT=" + SELL_LIMIT +
                ", BID_PRICE=" + BID_PRICE +
                ", BUY_LIMIT=" + BUY_LIMIT +
                ", ASK_PRICE=" + ASK_PRICE +
                ", MAX_PRICE_SHIFT=" + MAX_PRICE_SHIFT +
                ", EXCHANGE='" + EXCHANGE + '\'' +
                '}';
    }
}
