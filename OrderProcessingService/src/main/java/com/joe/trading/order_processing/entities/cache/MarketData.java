package com.joe.trading.order_processing.entities.cache;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private Double LAST_TRADED_PRICE;
    @JsonProperty
    private String TICKER;
    @JsonProperty
    private Integer SELL_LIMIT;
    @JsonProperty
    private Double BID_PRICE;
    @JsonProperty
    private Integer BUY_LIMIT;
    @JsonProperty
    private Double ASK_PRICE;
    @JsonProperty
    private Double MAX_PRICE_SHIFT;

    private String EXCHANGE;

    public MarketData(
            Double LAST_TRADED_PRICE,
            String TICKER,
            Integer SELL_LIMIT,
            Double BID_PRICE, Integer BUY_LIMIT, Double ASK_PRICE, Double MAX_PRICE_SHIFT) {
        this.LAST_TRADED_PRICE = LAST_TRADED_PRICE;
        this.TICKER = TICKER;
        this.SELL_LIMIT = SELL_LIMIT;
        this.BID_PRICE = BID_PRICE;
        this.BUY_LIMIT = BUY_LIMIT;
        this.ASK_PRICE = ASK_PRICE;
        this.MAX_PRICE_SHIFT = MAX_PRICE_SHIFT;
        this.EXCHANGE = null;
    }

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
